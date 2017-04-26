package GUI

import core._
import processing.core._
import processing.core.PConstants._
import scala.math.Pi
import scala.collection.mutable.Buffer

object Main extends PApplet {

  val w = 1280
  val h = 800
  val side = 32
  val off = 0.5f
  var clicks = 0
  
  /* Loading pictures for the enemies, turrets */
  val level1bg = loadImage("levels/Level1.png")
  val level2bg = loadImage("levels/Level2.png")
  val level3bg = loadImage("levels/Level3.png")
  val levelbgs = Map(1 -> level1bg, 2 -> level2bg, 3 -> level3bg)
  val pic1 = loadImage("enemies/Enemy1.png")
  val pic2 = loadImage("enemies/Enemy2.png")
  val pic3 = loadImage("enemies/Enemy3.png")
  val pics = Map("type1" -> pic1, "type2" -> pic2, "type3" -> pic3)
  val base = loadImage("towers/Tower.png")
  val turret = loadImage("towers/Turret.png")
  val coin = loadImage("etc/coin.png")
  val heart = loadImage("etc/heart.png")
  coin.resize(32, 0)

  spread

  val button1 = Button("Level 1", 640, 400, 80, 32)
  val button2 = Button("Level 2", 640, 450, 80, 32)
  val button3 = Button("Level 3", 640, 500, 80, 32)
  

  override def setup(): Unit = {
    size(w, h)
    frameRate(60) //60 fps MR
  }

  override def draw(): Unit = {
    if (!paused) {
      drawLevel(cl)
    }
    if (levels(cl).isDone) {
      button1.draw()
      button2.draw()
      button3.draw()
    }
    
    // Key handling has a delay so the same input isn't handled multiple times right away
    val time = System.currentTimeMillis()
    if (keyPressed && time - lastKeyPress >= 500) {
      lastKeyPress = time
      handleKey(key)
    }
  }

  override def mouseClicked(): Unit = {
    // mx and my give the coordinates of the nearest grid intersection
    val mx = (mouseX / side + off) * side
    val my = (mouseY / side + off) * side
    val pos = Vector2D(mx, my)
    if (!towerPos.contains(pos) && player.money >= costToBuild(selectedTowerType) && !printPos) {
      player.money -= costToBuild(selectedTowerType)
      towerPos += pos
      towers += placeTower(pos, selectedTowerType)
    } else if (towerPos.contains(pos)) {
      val tower = towers.find(t => t.pos == pos).get
      selectedTower = tower
    }
    
    if (levels(cl).isDone) {
      if (button1.mouseIsOver) {
        reload(1)
      } else if (button2.mouseIsOver) {
        reload(2)
      } else if (button3.mouseIsOver) {
        reload(3)
      }
    }

    /* This block is for the initial setup of the path for a new level */
    if (printPos) {
      if (clicks % 9 == 0) {
        println("")
        Console.print(s"Path : $mx, $my; ")
        //      Console.print(s"$mouseX, $mouseY; ")
      } else {
        //    Console.print(s"$mx, $my; ")
        Console.print(s"$mouseX, $mouseY; ")
      }
      clicks += 1
    }
  }

  private def drawLevel(level: Int) {
    image(levelbgs(level), 0, 0)
    drawGrid(h, w, side, off)
    cw.getEnemies.filter(_.cn > 0).filter(_.alive).foreach { e =>
      rot(e)
      drawHealth(e)
    }
    towers.foreach { t =>
      drawRange(t)
    }
    towers.foreach { t =>
      rot(t)
    }

    if (selectedTower != null) {
      drawRange(selectedTower)
    }
    drawTowerInfo(selectedTower)

    

    if (started) {
      cw.getEnemies.foreach(e => e.move)
      towers.foreach { t =>
        val deadBefore = cw.getEnemies.filter(_.dead)
        val dmg = t.shoot
        val deadAfter = cw.getEnemies.filter(_.dead)
        if (dmg != 0.0) line(t.x, t.y, t.lastTarget.x.toFloat, t.lastTarget.y.toFloat)
        val died = (deadAfter.toSet -- deadBefore).toVector
        if (died.size >= 1) player.money += died(0).moneyWorth
      }
      if (cw.isDone && levels(level).hasNext) {
        levels(level).nextWave
        spread
      }
    }
    drawInfo
  }
  
  def drawInfo = {
    image(coin, 17, 16)
    image(heart, 17, 48)
    textAlign(LEFT, TOP)
    textSize(30)
    fill(255, 255, 0)
    text(player.money, 50, 16)
    fill(255, 0, 0)
    text(player.lives, 50, 48)
  }
  
  def drawTowerInfo(t: Tower) = {
    textAlign(RIGHT, TOP)
    textSize(20)
    fill(255)
    val info = if (selectedTower == null) s"Tower build cost: ${costToBuild(selectedTowerType)}" 
      else s"Level: ${t.upLevel}\nDamage: ${to2Dec(t.damage)}\nRange: ${to2Dec(t.range)}\nRoF: ${to2Dec(t.rof)}\nUpgrade Cost: ${to2Dec(t.cost)}"
    text(info, 1264, 16)
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
    rotate((Pi / 2).toFloat)
    translate(-t.x, -t.y)
    image(turret, t.x, t.y)
    imageMode(CORNER)
    translate(t.x, t.y)
    rotate(-(t.theta + (Pi / 2).toFloat))
    translate(-t.x, -t.y)
  }

  private def drawGrid(hei: Int, wid: Int, size: Int, offset: Float) {
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

  private def drawRange(t: Tower) = {
    if (showRanges || selectedTower == t) {
      fill(150, 0, 0, 20)
      ellipseMode(RADIUS)
      ellipse(t.x, t.y, t.r, t.r)
    }
  }

  case class Button(label: String, x: Float, y: Float, w: Float, h: Float) {
    def mouseIsOver = mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h)
    def draw() {
      fill(218)
      stroke(141)
      rect(x, y, w, h, 10)
      textAlign(CENTER, CENTER)
      textSize(12)
      fill(0)
      text(label, x + (w / 2), y + (h / 2))
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

