
import java.io._
import scala.collection.mutable.Buffer

package object core {

  def loadLevel(level: Int): Level = {
    
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
                val cleaned = line.split(":").map(_.trim.toLowerCase)
                cleaned(0) match {
                  case "level" => 0  // not necessary since the level number is in the name of the file
                  case "path" => {
                    val coords = cleaned(1).split(";").map(_.split(",").map(_.trim.toInt))
                    for (c <- coords) {
                      nodes += Vector2D(c(0), c(1))
                    }
                    path = Path(nodes.toVector)
                  }
                }
              }
            }
            case "waves" => {
              for (line <- chunk if (line.trim.nonEmpty)) {
                
              }
            }
            case _ =>
          }
        }
        val l = Level(path)
        waves.foreach(w => l.addWave(w))
        waves.foreach { w => 
          w.setPaths(path)
          w.setPositions(path(0))
          }
        
        l
        
      } finally {
        file.close()
        reader.close()
      }

    } catch {
      case e: FileNotFoundException => throw new CorruptedTDFileException(s"File missing: $level.level")
      case e: IOException           => throw new CorruptedTDFileException("Read error.")
    }

  }
  
  def loadEnemy(enemyType: String): Enemy = {
    
    var health = 0
    var speed = Vector2D(0, 0)
    var maxSpeed = 0
    
    try {
      val file = new FileReader(s"enemies/$enemyType.enemy")
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
        
        val lines = wholeFile.split("\n")
        
        for (line <- lines) {
          val parts = line.split(":").map(_.trim.toLowerCase())
           parts(0) match {
            case "enemy type" => 0
            case "health" => health = parts(1).toInt
            case "speed" => {
              val s = parts(1).split(",").map(_.trim.toInt)
              speed = Vector2D(s(0), s(1))
            }
            case "maxspeed" => maxSpeed = parts(1).toInt
          }
        }
        
        val e = Enemy(health, speed)
        e.setMaxSpeed(maxSpeed)
        e
        
      } finally {
        file.close()
        reader.close()
      }

    } catch {
      case e: FileNotFoundException => throw new CorruptedTDFileException(s"File missing: $enemyType.enemy")
      case e: IOException           => throw new CorruptedTDFileException("Read error.")
    }
  }
  
  def trunc(v: Vector2D, s: Double): Vector2D = if (v.r > s) v.unit * s else v

  class CorruptedTDFileException(message: String) extends Exception(message)
}

