package Files

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.util.logging.FileHandler

object FileInsertExample extends App {
  // Define the file path
  val filePath = "output.txt"

  // Data to be inserted
  val dataToInsert = "Hello, World!\n"

  def writeTOfile(File:String,data:String):Unit ={
    // Use FileWriter and BufferedWriter to insert data into the file
    val fileWriter = new FileWriter (File, true) // The second parameter 'true' appends to the existing file
    val bufferedWriter = new BufferedWriter (fileWriter)
    try {
      bufferedWriter.write (data)
      println ("Data inserted successfully.")
    } finally {
      // Close the resources
      bufferedWriter.close ()
      fileWriter.close ()
    }
  }
  def ReadFromFile(File:String):Unit={
    try {
      val content =Files.readString(Paths.get(File),StandardCharsets.UTF_8)
      println(content)
    }catch {
      case e: Exception =>{
        println("Error!!!!!")
        None
      }
    }
  }

  private val fileHandler: FileHandler = new FileHandler()
  private val customersFilePath = "customers.txt"
  private val roomsFilePath = "rooms.txt"
  private val bookingsFilePath = "bookings.txt"

  def createFileIfNotExists(filePath: String): Unit = {
    val file = new File(filePath)
    if (!file.exists()) {
      try {
        val writer = new PrintWriter(file)
        writer.close()
      } catch {
        case e: Exception =>
          println(s"Error: Unable to create file - $filePath")
          throw e
      }
    }
  }

  def RemoveFromFile(data: String, filename: String): Unit = {
    // Read the contents of the file
    val file = new File(filename)
    val source = scala.io.Source.fromFile(filename)
    val lines = try source.mkString finally source.close()

    if (lines.contains(data)) {
      // Remove the specified data from the contents
      val updatedContents = lines.replace(data, "")

      // Write the updated contents back to the file
      val writer = new PrintWriter(file)
      try {
        writer.write(updatedContents)
        println(s"Data '$data' removed from the file.")
      } catch {
        case e: Exception =>
          println(s"Error: Unable to remove data from the file. Reason: ${e.getMessage}")
      } finally {
        writer.close()
      }
    } else {
      println(s"Data '$data' not found in the file.")
    }
  }

//  writeTOfile(filePath,dataToInsert)
//  ReadFromFile(filePath)

}
