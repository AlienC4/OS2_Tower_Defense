package core

case class Enemy(var health: Double, var speed: Vector2D, var pos: Vector2D) {
  
  private var path: Path = null
  private var currentNode: Int = 0
  private val distance = 10
  def cn = currentNode
  
  def alive = this.health > 0
  def dead = !alive
  
  def hit(damage: Double) = {
    val dmg = math.min(damage, health)
    health -= dmg
    dmg
  }
  
  def getPath = this.path
  def setPath(p: Path) = this.path = p
  
  /** Returns a vector pointing to the given target */
  def desiredVelocity(target: Vector2D) = (target - this.pos).unit * this.speed.r
  /** Steers towards the given target */
  def steering(target: Vector2D) = {
    this.desiredVelocity(target) - this.speed
  }
  
  /** Moves the enemy towards the targeted node */
  def move = {
    val target = this.path(cn)
    this.speed = this.steering(target) + this.speed
    this.pos += this.speed
    if ((this.pos.r - target.r) <= this.distance && this.cn < this.path.length) {
      this.currentNode += 1
    }
  }
  
  
}

trait Flying {
  val flying = true
}