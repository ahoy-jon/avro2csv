

/**
 *
 * eg:
 *
 * java ... AvroToCSV inputfile.avro outputfile.csv
 * java ... AvroToCSV inputfile.avro  -  # to stdout
 * java ... AvroToCSV - - # from stdin to stdout
 */
object AvroToCSV extends App {
  import scala.collection.JavaConverters._

  import org.apache.avro.tool.{Util, Tool, CsvTool}

  private val tool: CsvTool = new CsvTool

  tool.run(System.in,System.out,System.err, seqAsJavaListConverter(args.toList).asJava)

}


package org.apache.avro.tool {

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.PrintStream
import joptsimple.OptionParser
import joptsimple.OptionSet
import org.apache.avro.Schema
import org.apache.avro.file.DataFileStream
import org.apache.avro.generic.{GenericRecord, GenericDatumReader}
import org.apache.commons.lang.StringEscapeUtils

class CsvTool extends Tool {
  val LINE_SEPERATOR: Array[Byte] = System.getProperty("line.separator").getBytes

  val getName: String = "totext2"

  val getShortDescription: String = "Converts and avro file to a csv file."


  def run(stdin: InputStream, out: PrintStream, err: PrintStream, args: java.util.List[String]): Int = {

    import scala.collection.JavaConversions._
    val p: OptionParser = new OptionParser
    val opts: OptionSet = p.parse(args: _*)

    if (opts.nonOptionArguments.size != 2) {
      err.println("Expected 2 args: from_file to_file (local filenames," + " Hadoop URI's, or '-' for stdin/stdout")
      p.printHelpOn(err)
      return 1
    }
    val inStream: BufferedInputStream = Util.fileOrStdin(args.get(0), stdin)
    val outStream: BufferedOutputStream = Util.fileOrStdout(args.get(1), out)
    val reader: GenericDatumReader[AnyRef] = new GenericDatumReader[AnyRef]
    val fileReader: DataFileStream[AnyRef] = new DataFileStream[AnyRef](inStream, reader)
    val schema: Schema = fileReader.getSchema

    val fields = schema.getFields


    implicit def stringToBytes = (_: String).toCharArray.map(_.toByte)

    outStream.write(fields.map(_.name()).mkString(","))
    outStream.write(LINE_SEPERATOR)

    while (fileReader.hasNext) {
      val record: GenericRecord = fileReader.next().asInstanceOf[GenericRecord]

      val row: String = fields.map(f => Option(record.get(f.name())).map(
        v => StringEscapeUtils.escapeCsv(v.toString)
      ).getOrElse("")).mkString(",")

      outStream.write(row)
      outStream.write(LINE_SEPERATOR)
    }

    outStream.close()
    inStream.close()
    0
  }
}

}