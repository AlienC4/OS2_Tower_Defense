
import java.io._
import scala.collection.mutable.Buffer

package object core {

  def loadLevel(level: Int) = {
    
    var path: Path = null
    val nodes: Buffer[Vector2D] = Buffer()
    val waves: Buffer[Wave] = Buffer()

    try {
      val file = new FileReader(s"levels/$level.level")
      val reader = new BufferedReader(file)

      try {

        var currentLine = reader.readLine()

        val wholeFile = {
          val b = Buffer[String]()
          while (currentLine != null) {
            currentLine = reader.readLine()
//            if (currentLine != null && currentLine.startsWith("#")) currentLine = currentLine.replaceFirst("#", "#ph")
            b += currentLine
          }
          b -= null
          b.mkString("\n")
        }
        
        val chunked = wholeFile.split("#").drop(1).map(_.split("\n"))
        
        for (chunk <- chunked) {
          
          val name = chunk(0).toLowerCase().trim()
          name match {
            case "info" => {
              for (line <- chunk if (line.trim.nonEmpty)) {
                
              }
            }
            case "waves" => {
              for (line <- chunk if (line.trim.nonEmpty)) {
                
              }
            }
            case _ =>
          }
        }
        
        
      } finally {
        file.close()
        reader.close()
      }

    } catch {
      case e: FileNotFoundException => throw new CorruptedTDFileException(s"File missing: $level.level")
      case e: IOException           => throw new CorruptedTDFileException("Read error.")
    }

  }

  class CorruptedTDFileException(message: String) extends Exception(message)
}

