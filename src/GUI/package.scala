
import core._
import scala.math.Pi
import scala.collection.mutable.Buffer


package object GUI {
  
  val level1 = loadLevel(1)
  
  /* Setting up things for drawing towers */
  val towerPos = Buffer[(Int, Int)]()
  val towers = Buffer[Tower]()
  
  /*  */
  var showRanges = false
  var showHealth = false

  /* Shorten the name for current wave */
  def cw = level1.currentWave
  
  def spread = (0 until cw.size).foreach(i => cw(i).setPos(cw(i).getPos.offset(0, 10 * (i + 1))))
  
  def placeTower(pos: Vector2D, tower: String) = {
    val (range, damage, rof) = loadTower(tower)
    val t = new Tower(pos, range, damage, rof)
    t.level = level1
    t
  }
  
  
  case class HealthBar(value: Double, max: Double, x: Float, y: Float, w: Float, h: Float)
}