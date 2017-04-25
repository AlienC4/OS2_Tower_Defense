package GUI

import core._
import processing.core._
import processing.core.PConstants._
import scala.math.Pi
import scala.collection.mutable.Buffer

object Main extends PApplet {

  val w = 1280
  val h = 800
  
  /* Loading pictures and the current level */
  val bg = loadImage("levels/Level1.png")
  val pic1 = loadImage("enemies/Enemy1.png")
  val pic2 = loadImage("enemies/Enemy2.png")
  val pic3 = loadImage("enemies/Enemy3.png")
  val pics = collection.Map("type1" -> pic1, "type2" -> pic2, "type3" -> pic3)
  val base = loadImage("towers/Tower.png")
  val turret = loadImage("towers/Turret.png")
  
  spread

  val side = 32
  val off = 0.5f


  override def setup(): Unit = {
    size(w, h)
    frameRate(60) //60 fps MR
  }

  override def draw(): Unit = {
    image(bg, 0, 0)
    drawGrid(h, w, side, off)
    cw.getEnemies.filter(_.alive).foreach { e => 
      rot(e) 
      drawHealth(e)
    }
    towers.foreach { t =>
      drawRange(t)
      rot(t)
    }
    if (millis() >= 5000) {
      cw.getEnemies.foreach(e => e.move)
      towers.foreach(t => if (t.shoot != 0.0) line(t.x, t.y, t.lastTarget.x.toFloat, t.lastTarget.y.toFloat))
      if (cw.isDone && level1.hasNext) {
        level1.nextWave
        spread
      }
    }
  }
  
  override def mouseClicked(): Unit = {
    // mx and my give the coordinates of the nearest point on the grid
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

  /** Rotates the given enemy around it's center point */
  private def rot(e: Enemy): Unit = {
    translate(e.x, e.y)
    rotate((e.speed.theta + Pi).toFloat)
    rotate(Pi.toFloat)
    translate(-e.x, -e.y)
    imageMode(CENTER)
    image(pics(e.etype), e.x, e.y)
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
  
  def drawGrid(hei: Int, wid: Int, size: Int, offset: Float) {
    stroke(0, 0, 0)
    for (i <- 1 to hei / size) {
      line(0, (i - offset) * size, w, (i - offset) * size)
    }
    for (i <- 1 to wid / size) {
      line((i - offset) * size, 0, (i - offset) * size, h)
    }
  }
  
  private def drawHealth(e: Enemy) = {
    val hb = new HealthBar(e.health, e.initHealth, e.x - 16, e.y - 32, side.toFloat, 10f)
    val curHealth = (hb.value / hb.max * hb.w).toFloat
    if (hb.value < hb.max && showHealth) {
      fill(255, 0, 0)
      stroke(0)
      rect(hb.x, hb.y, hb.w, hb.h)
      fill(0, 255, 0)
      rect(hb.x, hb.y, curHealth, hb.h)
    }
  }
  
  def drawRange(t: Tower) = {
    if (showRanges) {
      fill(150, 0, 0, 50)
      ellipseMode(RADIUS)
      ellipse(t.x, t.y, t.r, t.r)
    }
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

