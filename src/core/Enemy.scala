package core

case class Enemy(initHealth: Double, var speed: Vector2D, var pos: Vector2D, moneyWorth: Int, livesWorth: Int, etype: String) {

  private var path: Path = null
  private var currentNode: Int = 0
  private val distance = 8
  private var maxSpeed: Double = 1.0

  var health = initHealth

  def cn = currentNode

  def alive = this.health > 0
  def dead = !alive
  def isAtEndOfPath = cn == path.length

  def hit(damage: Double) = {
    val dmg = math.min(damage, health)
    health -= dmg
    dmg
  }

  def getPath = this.path
  def setPath(p: Path) = this.path = p
  def setMaxSpeed(s: Double) = maxSpeed = s
  def setPos(v: Vector2D) = this.pos = v
  def getPos = this.pos

  /** Returns a vector pointing to the given target */
  def desiredVelocity(target: Vector2D) = (target - this.pos).unit * this.speed.r
  /** Steers towards the given target */
  def steering(target: Vector2D) = {
    (this.desiredVelocity(target) - this.speed) / 15
  }

  /** Moves the enemy towards the targeted node */
  def move = {
    if (!isAtEndOfPath && this.alive) {
      val target = this.path(cn)
      this.speed += this.steering(target)
      if (target.isWithin(32, this.pos) && this.speed.r * 1.05 >= maxSpeed) this.speed *= 0.8 else this.speed *= 1.05
      
      this.speed = trunc(this.speed, maxSpeed)
      this.pos += this.speed
      if (target.isWithin(distance, this.pos) && this.cn < this.path.length) {
        this.currentNode += 1
      }
    }
  }
  
  def x = this.pos.x.toFloat
  def y = this.pos.y.toFloat

}

trait Flying {
  val flying = true
}