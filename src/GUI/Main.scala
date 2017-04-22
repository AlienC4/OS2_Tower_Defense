package GUI

import core._
import processing.core._
import java.awt.Dimension

object Main extends PApplet {

  val w = 1280
  val h = 800
  val bg = loadImage("levels/Level1.png")
  bg.resize(w, h)
  val level = loadLevel(1)
  
  val side = 32
  val colCount = w / side
  val rowCount = h / side

  def drawGrid(hei: Int, wid: Int, size: Int, offset: Float) {
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
    stroke(255, 255, 255)
  }

  override def draw(): Unit = {
    image(bg, 0, 0)

    drawGrid(h, w, side, 0.5f)
  }

  override def mouseClicked(): Unit = {
    val mx = (mouseX / side + 0.5) * side
    val my = (mouseY / side + 0.5) * side
    Console.print(s"$mx, $my; ")
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