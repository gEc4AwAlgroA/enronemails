package main.scala.enronemails

object Main {
  import scala.xml.XML
  import java.io._

  def main(args: Array[String]) {
    // FUTURE - check correct arg length and that argument is a directory
    val dirPath: String = args(0)
    val numberToReturn: Int = args(1).toInt
    val dirList: List[java.io.File] = new java.io.File(dirPath).listFiles.filter(_.getName.endsWith(".xml")).toList
    // This requires more memory than the t2.micro I'm using - a larger instance should cope with all-at-once
    //    val messageList: List[scala.xml.NodeSeq] = dirList.map(getMessagesFromFile(_))
    //    val emailList: Array[(String, Double)] =
    //      messageList
    //      .flatMap(x => getAddresseeList(x))
    //      .groupBy(_._1)
    //      .mapValues(_.map(_._2).sum)
    //      .toArray.sortBy(_._2).reverse
    //    val countSumList: List[(Int, Int)] = messageList.map(x => getSumCount(x))
    //
    //    val (totalCount, totalSum): (Int, Int) = countSumList.reduce((a, b) => (a._1 + b._1, a._2 + b._2))
    //    println(s"Average email size ${totalSum/totalCount}")
    //    emailList.take(numberToReturn).foreach(println)
    def iter(dirList:List[java.io.File], accCountSum: List[(Long, Long)],
             accAddresses: List[(String, Double)]): (List[(Long,Long)], List[(String, Double)]) = {
      dirList match {
        case List() => (accCountSum, accAddresses)
        case head :: tail =>
          println(s"Reading $head")
          val messages = getMessagesFromFile(head)
          iter(tail, getSumCount(messages) :: accCountSum, getAddresseeList(messages) ++ accAddresses)
      }
    }
    val (countSumList, emailList): (List[(Long, Long)],  List[(String, Double)]) = iter(dirList, List(), List())

    val mostEmailed: List[(String, Double)] = emailList.groupBy(_._1).mapValues(_.map(_._2).sum).toList.sortBy(_._2).reverse
    val (totalCount, totalSum): (Long, Long) = countSumList.reduce((a, b) => (a._1 + b._1, a._2 + b._2))

    val outputFileName = "output.txt"
    val pwhandle = new PrintWriter(new File(outputFileName))
    pwhandle.println(s"Number of emails $totalCount")
    pwhandle.println(s"Total size $totalSum")
    pwhandle.println(s"Average email size ${totalSum/totalCount}")
    mostEmailed.take(numberToReturn).foreach(x => pwhandle.println(x._2, x._1))
    pwhandle.close

    println(s"Finished - see: $outputFileName")
  }

  val messageIdentifier:String = "Message"
  val tagNameTo:String = "#To"
  val tagNameCC:String = "#CC"
  val weightTo:Double = 1
  val weightCC:Double = 0.5
  val filePathText = "Text"

  def getMessagesFromFile(handle: java.io.File): scala.xml.NodeSeq = (XML.loadFile(handle) \ "Batch" \ "Documents" \ "Document")
    .filter (_.attribute("DocType").getOrElse("").toString == messageIdentifier)

  def getAddresseeListInt(messageList: scala.xml.NodeSeq, tagName: String, weight: Double): Seq[(String, Double)] = {
    (messageList \ "Tags" \ "Tag")
      .filter(_.attribute("TagName").getOrElse("").toString == tagName)
      .map(_.attribute("TagValue").getOrElse("").toString)
      //      .flatMap(_.split("(?<=;), "))
      .flatMap(_.split(","))
      .map(_.replaceAll("&lt;.*&gt;", "")).map(_.replaceAll(" +", " ")).map(_.replaceAll("[']", "")).map(_.trim)
      .map(x => (x, weight))
  }
  def getAddresseeList(messageList: scala.xml.NodeSeq): List[(String, Double)] = {
    messageList
      .flatMap(x => getAddresseeListInt(x, tagNameTo, weightTo) ++ getAddresseeListInt(x, tagNameCC, weightCC))
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum).toList
  }

  def getSumCount(messageList: scala.xml.NodeSeq): (Long, Long) = {
    val sizeList = ((messageList \ "Files" \ "File")
      .filter (_.attribute("FileType").getOrElse("").toString == filePathText) \ "ExternalFile")
      .map(_.attribute("FileSize").getOrElse("").toString.toLong)
    (sizeList.length.toLong, sizeList.sum)
  }

}
