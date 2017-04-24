package GUI

import core._
import processing.core._
import processing.core.PConstants._
import scala.math.Pi
import scala.collection.mutable.Buffer

object Main extends PApplet {

  val w = 1280
  val h = 800
  val bg = loadImage("levels/Level1.png")
  val level1 = loadLevel(1)
  val enemy1 = loadEnemy("type1")
  val pic1 = loadImage("enemies/Enemy1.png")
  val tower1 = loadTower("basic")
  val base = loadImage("towers/Tower.png")
  val turret = loadImage("towers/Turret.png")
  pic1.resize(32, 0)
  val towerPos = Buffer[(Int, Int)]()
  val towers = Buffer[Tower]()
  var showRanges = false

  def cw = level1.currentWave
  
  for (i <- 0 until cw.size) {
    cw(i).setPos(cw(i).getPos.offset(0, 10 * (i + 1)))
  }

  val side = 32
  val off = 0.5f

  def drawGrid(hei: Int, wid: Int, size: Int, offset: Float) {
    stroke(0, 0, 0)
    for (i <- 1 to hei / size) {
      line(0, (i - offset) * size, w, (i - offset) * size)
    }
    for (i <- 1 to wid / size) {
      line((i - offset) * size, 0, (i - offset) * size, h)
    }
  }

  override def setup(): Unit = {
    size(w, h)
    frameRate(60) //60 fps MR
  }

  override def draw(): Unit = {
    image(bg, 0, 0)
    drawGrid(h, w, side, off)
    cw.getEnemies.foreach { e => 
      rot(e) 
      drawHealth(e)
    }
    towers.foreach { t =>
      rot(t)
    }
    if (millis() >= 5000) {
      cw.getEnemies.foreach(e => e.move)
      towers.foreach(t => t.shoot)
    }
      
  }

  /** Rotates the given enemy around it's center point */
  private def rot(e: Enemy): Unit = {
    translate(e.x, e.y)
    rotate((e.speed.theta + Pi).toFloat)
    rotate(Pi.toFloat)
    translate(-e.x, -e.y)
    imageMode(CENTER)
    image(pic1, e.x, e.y)
    imageMode(CORNER)
    translate(e.x, e.y)
    rotate(-(e.speed.theta + 2 * Pi).toFloat)
    translate(-e.x, -e.y)
  }
  
  private def rot(t: Tower): Unit = {
    imageMode(CENTER)
    image(base, t.x, t.y)
    translate(t.x, t.y)
    rotate(t.theta)
    rotate((Pi/2).toFloat)
    translate(-t.x, -t.y)
    image(turret, t.x, t.y)
    imageMode(CORNER)
    translate(t.x, t.y)
    rotate(-(t.theta + (Pi/2).toFloat))
    translate(-t.x, -t.y)
  }
  
  private def drawHealth(e: Enemy) = {
    val hb = new HealthBar(e.health, e.initHealth, e.x - 16, e.y - 32, side.toFloat, 10f)
    val curHealth = (hb.value / hb.max * hb.w).toFloat
    if (hb.value < hb.max) {
      fill(255, 0, 0)
      stroke(0)
      rect(hb.x, hb.y, hb.w, hb.h)
      fill(0, 255, 0)
      rect(hb.x, hb.y, curHealth, hb.h)
    }
  }

  override def mouseClicked(): Unit = {
    val mx = (mouseX / side + off) * side
    val my = (mouseY / side + off) * side
    val pos = Vector2D(mx, my)
    val p = (mx.toInt, my.toInt)
    if (!towerPos.contains(p)) {
      towerPos += p
      towers += placeTower(pos, "basic")
    }
//    Console.print(s"$mx, $my; ")
//    Console.print(s"$mouseX, $mouseY; ")
  }
  
  
  def placeTower(pos: Vector2D, tower: String) = {
    val (range, damage, rof) = loadTower(tower)
    val t = new Tower(pos, range, damage, rof)
    t.level = level1
    t
  }

  def main(args: Array[String]) {
    val frame = new javax.swing.JFrame("Super Awesome Tower Defense")

    frame.getContentPane().add(this)
    init
    setup
    frame.setSize(this.getSize())
    frame.pack
    frame.setVisible(true)
    frame.setLocationRelativeTo(null)
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE)
  }

}

case class HealthBar(value: Double, max: Double, x: Float, y: Float, w: Float, h: Float)