package main.scala.enronemails

object Main {
  import scala.xml.XML

  def main(args: Array[String]) {
    // FUTURE - check correct arg length and that argument is a directory
    val dirPath: String = args(0)
    val numberToReturn: Int = args(1).toInt
    val dirList:Array[java.io.File] = new java.io.File(dirPath).listFiles.filter(_.getName.endsWith(".xml"))
    val messageList: Array[scala.xml.NodeSeq] = dirList.map(XML.loadFile(_)).map(getMessages(_))
    val emailList: Array[(String, Double)] =
      messageList
      .flatMap(x => getAddresseeList(x, tagNameTo, weightTo) ++ getAddresseeList(x, tagNameCC, weightCC))
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum)
      .toArray.sortBy(_._2).reverse
    val countSumList: Array[(Int, Int)] = messageList.map(x => getSumCount(x))

    val (totalCount, totalSum): (Int, Int) = countSumList.reduce((a, b) => (a._1 + b._1, a._2 + b._2))
    println(s"Average email size ${totalSum/totalCount}")
    emailList.take(numberToReturn).foreach(println)
  }

  val messageIdentifier:String = "Message"
  val tagNameTo:String = "#To"
  val tagNameCC:String = "#CC"
  val filePathText = "Text"
  val weightTo:Double = 1
  val weightCC:Double = 0.5

  def getMessages(xmlData: scala.xml.Elem): scala.xml.NodeSeq = (xmlData \ "Batch" \ "Documents" \ "Document")
    .filter (_.attribute("DocType").getOrElse("").toString == messageIdentifier)

  def getAddresseeList(messageList: scala.xml.NodeSeq, tagName: String, weight: Double): Seq[(String, Double)] = {
    (messageList \ "Tags" \ "Tag")
      .filter(_.attribute("TagName").getOrElse("").toString == tagName)
      .map(_.attribute("TagValue").getOrElse("").toString)
//      .flatMap(_.split("(?<=;), "))
      .flatMap(_.split(","))
      .map(_.replaceAll("&lt;.*&gt;", "")).map(_.replaceAll(" +", " ")).map(_.replaceAll("[']", "")).map(_.trim)
      .map(x => (x, weight))
  }

  def getSumCount(messageList: scala.xml.NodeSeq): (Int, Int) = {
    val sizeList = ((messageList \ "Files" \ "File")
      .filter (_.attribute("FileType").getOrElse("").toString == filePathText) \ "ExternalFile")
      .map(_.attribute("FileSize").getOrElse("").toString.toInt)
    (sizeList.length, sizeList.sum)
  }

}
