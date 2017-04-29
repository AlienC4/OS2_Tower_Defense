
import core._
import scala.math.Pi
import scala.collection.mutable.Buffer


package object GUI {
  
  /* Loads the levels */
  val level1 = loadLevel(1)
  val level2 = loadLevel(2)
  val level3 = loadLevel(3)
  val levels = Map(1 -> level1, 2 -> level2, 3 -> level3)
  
  /** The current level number, can be any of the keys of levels */
  var cl = 1
  
  /* Initializing the player */
  val player = Player(levels(cl).money, levels(cl).lives)
  
  /* Setting up things for drawing and handling towers */
  val towers = Buffer[Tower]()
  val towerPos = Buffer[Vector2D]()
  var selectedTowerType = "basic"
  var selectedTower: Tower = null
  
  
  /* Various settings */
  var showRanges = false
  var showHealth = true
  var started = false
  var paused = false // Currently not working
  val printPos = false
  
  /** Variable for storing the last time a key was pressed so a delay can be added */
  var lastKeyPress = System.currentTimeMillis()

  /* Shorten the name for current wave */
  def cw = levels(cl).currentWave
  
  /** Spreads out the enemies in the current wave */
  def spread = (0 until cw.size).foreach(i => cw(i).setPos(cw(i).pos.offset(0, 50 * (i + 1))))
  
  /** Checks if the game is over */
  def gameOver = player.lives <= 0
  
  /** Gets the cost to build a given tower */
  def costToBuild(tower: String) = {
    loadTower(tower)._1
  }
  
  /** Places a tower in the spot specified by pos */
  def placeTower(pos: Vector2D, tower: String) = {
    val (cost, range, damage, rof) = loadTower(tower)
    val t = new Tower(pos, cost, range, damage, rof)
    t.level = levels(cl)
    t
  }
   /** Handles keyboard inputs */
  def handleKey(key: Char) = {
    key match {
      case 's' => started = true
      case 'p' => paused = !paused
      case 'r' => showRanges = !showRanges
      case 'h' => showHealth = !showHealth
      case 'u' => if (selectedTower != null && player.money >= selectedTower.cost) {
        player.money -= selectedTower.cost
        selectedTower.upgrade
      }
      case 'd' => deselectChosen
      case '1' => selectedTowerType = "basic"
      case _ => 
    }
  }
  
  /** Rounds doubles to 2 decimal places */
  def to2Dec(d: Double) = (d * 100).round / 100.0
  
  /** Deselects the chosen tower */
  def deselectChosen = {
    selectedTower = null
  }
  
  /** Handles everything necessary for starting a new level */
  def reload(n: Int) = {
    cl = n
    started = false
    selectedTower = null
    levels(cl).reload
    spread
    towers.clear
    towerPos.clear()
    player.reload(levels(cl))
  }
  
  /** A class for representing healthbars */
  case class HealthBar(value: Double, max: Double, x: Float, y: Float, w: Float, h: Float) 
}