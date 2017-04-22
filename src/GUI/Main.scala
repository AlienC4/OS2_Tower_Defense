package GUI

import core._
import processing.core._
import processing.core.PConstants._
import java.awt.Dimension

object Main extends PApplet {

  val w = 1280
  val h = 800
  val bg = loadImage("levels/Level1.png")
  bg.resize(w, h)
  val level = loadLevel(1)
  val enemy = loadEnemy("type1")
  val pic = loadImage("enemies/Enemy1.png")
  pic.resize(32, 0)
  enemy.setPath(level.path)
  
  enemy.pos = Vector2D(400, 464).offset(0, math.random)
  
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
    translate(enemy.pos.x.toFloat, enemy.pos.y.toFloat)
    rotate((enemy.speed.theta + 180).toRadians.toFloat)
    rotate(180.toRadians.toFloat)
    translate(-enemy.pos.x.toFloat, -enemy.pos.y.toFloat)
    imageMode(CENTER)
//    if (millis() % 100 == 0)  println(enemy.speed.theta + 180)
    image(pic, enemy.pos.x.toFloat, enemy.pos.y.toFloat)
    
    imageMode(CORNER)
    
//    rect(48,48,96,96)
    if (millis() >= 5000)
    enemy.move
    
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