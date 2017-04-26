
import core._
import scala.math.Pi
import scala.collection.mutable.Buffer


package object GUI {
  
  val level1 = loadLevel(1)
  val level2 = loadLevel(2)
  val level3 = loadLevel(3)
  val levels = Map(1 -> level1, 2 -> level2, 3 -> level3)
  
  /** The current level number, can be any of the keys of levels */
  var cl = 3
  
  /* Initializing the player */
  var player = Player(levels(cl).money, levels(cl).lives)
  
  /* Setting up things for drawing towers */
  val towers = Buffer[Tower]()
  val towerPos = Buffer[Vector2D]()
  
  
  /*  */
  var showRanges = false
  var showHealth = true
  var started = false
  var paused = false
  var selectedTowerType = "basic"
  var selectedTower: Tower = null
  val printPos = false
  
  
  var lastKeyPress = System.currentTimeMillis()

  /* Shorten the name for current wave */
  def cw = levels(cl).currentWave
  
  def spread = (0 until cw.size).foreach(i => cw(i).setPos(cw(i).getPos.offset(0, 50 * (i + 1))))
  
  def costToBuild(tower: String) = {
    loadTower(selectedTowerType)._1
  }
  
  def placeTower(pos: Vector2D, tower: String) = {
    val (cost, range, damage, rof) = loadTower(tower)
    val t = new Tower(pos, cost, range, damage, rof)
    t.level = levels(cl)
    t
  }
  
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
      case '1' => selectedTowerType = "basic"
      case _ => 
    }
  }
  
  def to2Dec(d: Double) = (d * 100).toInt / 100.0
  
  def reload(n: Int) = {
    cl = n
    started = false
    levels(cl).reload
    spread
    towers.clear
    towerPos.clear()
    player.reload()
  }
  
  
  case class HealthBar(value: Double, max: Double, x: Float, y: Float, w: Float, h: Float)
}