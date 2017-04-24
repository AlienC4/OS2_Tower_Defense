
import java.io._
import scala.collection.mutable.Buffer

package object core {
  
  /**
   *  Loads the level given as parameter from the corresponding file
   *  @param level: the level number of the level to be loaded
   */
  def loadLevel(level: Int): Level = {
    
    var path: Path = Path(Vector())
    val nodes: Buffer[Vector2D] = Buffer()
    val waves: Buffer[Wave] = Buffer()
    def enemies = 
      collection.Map("type1" -> loadEnemy("type1"), 
                     "type2" -> loadEnemy("type2"),
                     "type3" -> loadEnemy("type3")) // only three possible enemy types currently

    try {
      val file = new FileReader(s"levels/$level.level")
      val reader = new BufferedReader(file)

      try {

        var currentLine = reader.readLine()
        val wholeFile = {
          val b = Buffer[String]()
          while (currentLine != null) {
            currentLine = reader.readLine()
            b += currentLine
          }
          b -= null
          b.map(_.takeWhile(_ != "§")).mkString("\n")
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
                    val coords = cleaned(1).split(";").map(_.split(",").map(_.trim.toDouble))
                    for (c <- coords) {
                      nodes += Vector2D(c(0), c(1))
                    }
                    path = Path(nodes.toVector)
                  }
                  case _ =>
                }
              }
            }
            case "waves" => {
              for (line <- chunk if (line.trim.nonEmpty)) {
                val cleaned = line.split(":").map(_.trim.toLowerCase)
                cleaned(0).takeWhile(_ != ' ') match {
                  case "wave" => {
                    val types = cleaned(1).split(";").map(_.trim.split(",")).map(x => (x(0).trim, x(1).trim.toInt))
                    var wave = Vector[Enemy]()
                    types.foreach(t => wave ++= Vector.fill(t._2)(enemies(t._1)))
                    waves += Wave(wave)
                  }
                  case _ => 
                }
              }
            }
            case _ =>
          }
        }
        val l = Level(path)
        waves.foreach{w => 
          l.addWave(w)
          w.setPaths(path)
          w.setPositions(path(0))
//          w.getEnemies.foreach(e => e.pos = e.pos.offset(0, math.random * 10))
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
  
  /**
   *  Loads the enemy given as parameter from the corresponding file
   *  @param enemyType: the type of the enemy to be loaded
   */
  def loadEnemy(enemyType: String): Enemy = {
    
    var health = 0
    var speed = Vector2D(0, 0)
    var maxSpeed = 0.0
    
    try {
      val file = new FileReader(s"enemies/$enemyType.enemy")
      val reader = new BufferedReader(file)

      try {

        var currentLine = reader.readLine()

        val wholeFile = {
          val b = Buffer[String]()
          while (currentLine != null) {
            currentLine = reader.readLine()
            b += currentLine
          }
          b -= null
          b.map(_.takeWhile(_ != "§")).mkString("\n")
        }
        
        val lines = wholeFile.split("\n")
        
        for (line <- lines if (line.trim.nonEmpty)) {
          val parts = line.split(":").map(_.trim.toLowerCase())
           parts(0) match {
            case "enemy type" => 0
            case "health" => health = parts(1).toInt
            case "speed" => {
              val s = parts(1).split(",").map(_.trim.toDouble)
              speed = Vector2D(s(0), s(1))
            }
            case "maxspeed" => maxSpeed = parts(1).toDouble
            case _ => 
          }
        }
        val e = new Enemy(health, speed, Vector2D(0, math.random))
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
  
  /** 
   *  Loads the tower's attributes from the corresponding file
   *  @param tower: the type of the tower to be loaded
   */
  def loadTower(tower: String) = {
    
  var range = 0.0
  var damage = 0.0
  var rof = 0.0
    
    try {
      val file = new FileReader(s"towers/$tower.tower")
      val reader = new BufferedReader(file)

      try {

        var currentLine = reader.readLine()

        val wholeFile = {
          val b = Buffer[String]()
          while (currentLine != null) {
            currentLine = reader.readLine()
            b += currentLine
          }
          b -= null
          b.map(_.takeWhile(_ != "§")).mkString("\n")
        }
        
        val lines = wholeFile.split("\n")
        
        for (line <- lines if (line.trim.nonEmpty)) {
          val parts = line.split(":").map(_.trim.toLowerCase())
          parts(0) match {
            case "range" => range = parts(1).toDouble
            case "damage" => damage = parts(1).toDouble
            case "rof" => rof = parts(1).toDouble
            case _ =>
          }
        }
        
        (range, damage, rof)
      } finally {
        file.close()
        reader.close()
      }

    } catch {
      case e: FileNotFoundException => throw new CorruptedTDFileException(s"File missing: $tower.tower")
      case e: IOException           => throw new CorruptedTDFileException("Read error.")
    }
    
  }
  
  def trunc(v: Vector2D, s: Double): Vector2D = if (v.r > s) v.unit * s else v

  class CorruptedTDFileException(message: String) extends Exception(message)
}

