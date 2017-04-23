package GUI

import core._
import processing.core._
import processing.core.PConstants._
import scala.math.Pi
import java.awt.Dimension

object Main extends PApplet {

  val w = 1280
  val h = 800
  val bg = loadImage("levels/Level1.png")
  val level1 = loadLevel(1)
  val enemy1 = loadEnemy("type1")
  val pic1 = loadImage("enemies/Enemy1.png")
  pic1.resize(32, 0)
  println(level1.getWave(0))
  def cw = level1.currentWave
//  enemy1.setPath(level1.path)
//
//  enemy1.pos = level1.path(0).offset(0, -math.random * 10)

  val side = 32
  val off = 0.5f

  def drawGrid(hei: Int, wid: Int, size: Int, offset: Float) {
    stroke(0x000000)
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
      imageMode(CENTER)
      image(pic1, e.x, e.y)
      imageMode(CORNER)
    }

    if (millis() >= 5000)
      cw.getEnemies.foreach(e => e.move)
  }

  /** Rotates the given enemy around it's center point */
  private def rot(e: Enemy) = {
    translate(e.x, e.y)
    rotate((e.speed.theta + Pi).toFloat)
    rotate(Pi.toFloat)
    translate(-e.x, -e.y)
  }

  override def mouseClicked(): Unit = {
    val mx = (mouseX / side + off) * side
    val my = (mouseY / side + off) * side
    //    Console.print(s"$mx, $my; ")
    Console.print(s"$mouseX, $mouseY; ")
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