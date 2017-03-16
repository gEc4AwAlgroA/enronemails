# enronemails

* This code can be used to perform some analysis on the Enron Email Corpus. It uses the metadata not the raw files.
* It currently implements a sequential approach to allow running in a limited memory environment but there is commented code to show an alternative approach which is closer to a big data / distributed pattern.
* There are some preliminary steps required to ingest the required data from AWS snapshot as desribed below.
* The code can be built using `sbt assembly` to give fat jar with no external dependencies.
* Usage ```java -jar enronemails-assembly-0.1-SNAPSHOT.jar input_path number_results```
* It currently logs progress by outputting each filename as processed and output is saved at output.txt with Total count, Total Size
and Average Size followed by list of number_results top emai recipients.
* It is not compete as there remain issues with the delimiting and tokenisation of email addresses.
For example the top 100 contains both "Steven J Kean" and "skean@enron.com".
There are also some bad records like "John" where listings in the form "surname, firstname" have been mangled.
An alternative approach - mapping to canonical form is included in the code but currently commented out.

## Data Exploration

* Snapshot appears to contain 2 versions - v1 .pst only and v2 with .pst and .xml metadata and raw text and attachments
* v2 seems to be the easier to use as the xml metadata can be readily parsed with scala-xml
* Total size of metadata <4GB so 8GB root volume sufficient if unzip only the xml
* Obvious at this stage that email address delimiting an issue
* Identification of email text size from ExternalFile's FileSize attribute where parent File's FileType="Text"
* I believe that there are no duplicate emails in this version of the corpus. Research suggests that the data is only deduped
within each custodian but searching through the corpus for subject lines, document ids, etc sent to wide internal distribution
list only finds different messages in the same thread. Possible I've missed a unique identifier.
* As well as the zips of metadata and raw content there are a number of other files within the v2 data. These appear to relate
to the original processing of this data and there are files that appear to relate to ids, uniqueness and deuplication. At this stage
it's not obvious exactly what they mean.

## Preliminary steps
1. Create EC2 instance
2. Create and attach ebs volume from snapshot
3. Extract xml metadata
4. Detach and delete volume
5. Run scala code
6. Delete instance

## 1. Create EC2 instance
* Assume awscli installed and configured with credentialsthat have necessary permissions (EC2 full should be sufficient)
* Keypair exists with private key per ssh path (here 'test') and public key imported to aws as named (here 'test')
* Security group 'inbound-ssh-all' must exist and allow access on port 22 from required ip addresses.
* must be in us-east-1 to access snapshot without copying
* jq is installed for json traversal (alternatively run the command without piping to jq and load the variable manually by inspection)

```sh
INSTANCEID=$(aws ec2 run-instances \
  --image-id "ami-0b33d91d" \
  --key-name test \
  --security-groups "inbound-ssh-all" \
  --instance-type "t2.micro" \
  --region "us-east-1" \
| jq -r '.Instances[].InstanceId)

PUBLICDNSNAME=$(aws ec2 describe-instances --region=us-east-1 --instance-ids="$INSTANCEID" \
| jq -r '.Reservations[].Instances[].NetworkInterfaces[].Association.PublicDnsName')
```

## 2. Create and attach ebs volume from snapshot
```sh
AVAILABILITYZONE=$(aws ec2 describe-instances --region=us-east-1 --instance-ids="$INSTANCEID" \
| jq -r '.Reservations[].Instances[].Placement.AvailabilityZone')

VOLUMEID=$(aws ec2 create-volume \
  --availability-zone $AVAILABILITYZONE \
  --volume-type "gp2" \
  --snapshot-id "snap-d203feb5" \
  --region "us-east-1" \
| jq -r '.VolumeId')

#volume needs to be available so wait 1 second before attaching
sleep 1
aws ec2 attach-volume \
  --volume-id $VOLUMEID \
  --instance-id "$INSTANCEID" \
  --device xvdf \
  --region "us-east-1"
```

## 3. Extract xml metadata
```sh
# will require user input first time or StrictHostKeyChecking=no
ssh -i test ec2-user@$PUBLICDNSNAME

mkdir -p /data
sudo mount /dev/xvdf /data
mkdir -p ~/enronxmldata/misc

#extract .xml files only from each *xml.zip
find /data/edrm-enron-v2 -name edrm-enron-v2_*_xml.zip |
while read filename
do
  echo "unzipping $filename"
  unzip $filename '*.xml' -x '*/*' -d ~/enronxmldata
done

#take 'misc bits' for data exploration
find /data/edrm-enron-v2 ! -name edrm* |
while read filename
do
  cp "$filename" ~/enronxmldata/misc
done
```

## 4. Detach and delete volume

```sh
sudo umount /data
# return to local machine
exit

aws ec2 detach-volume \
  --volume-id $VOLUMEID \
  --region "us-east-1"

#volume needs to be detached so wait 1 second before deleting
sleep 1
aws ec2 delete-volume \
  --volume-id $VOLUMEID \
  --region "us-east-1"
```

## 5. Run scala code

checkout code and build jar

```sh
# transfer jar to ec2 instance
scp -i test enronemails-assembly-0.1-SNAPSHOT.jar ec2-user@$PUBLICDNSNAME:~

ssh -i test ec2-user@$PUBLICDNSNAME
java -jar enronemails-assembly-0.1-SNAPSHOT.jar enronxmldata 100

# inspect output.txt or copy to local machine as required
exit
```

## 6. Delete instance

```sh
aws ec2 terminate-instances \
  --instance-ids="$INSTANCEID" \
  --region=us-east-1
```
