package core


/**
 * @params pos: The position of the tower
 * @param cost: The cost to place or upgrade the tower
 * @param range: The attack range of the tower
 * @param damage: the attack damage of the tower
 * @param rof: The rate of fire of the tower, in shots per second
 */
case class Tower(pos: Vector2D, var cost: Int, var range: Double, var damage: Double, var rof: Double) {
  
  private var lastShot = System.currentTimeMillis() // Initially the time when the tower was created
  var lastTarget: Vector2D = Vector2D(0, 0)
  /** Time between shots, in milliseconds */
  private def tbs = 1 / rof * 1000
  var level: Level = null
  var upLevel = 1
  
  
  def upgrade = {
    this.upLevel += 1
    this.range *= 1.1
    this.damage *= 1.3
    this.rof += 0.05
    this.cost = (1.4 * this.cost).toInt
  }
  
  def shoot = {
    val shot = System.currentTimeMillis()
    if (target.isDefined && target.get.alive && target.get.cn > 0 && (shot - lastShot) > tbs) {
      lastShot = shot
      lastTarget = target.get.pos
      target.get.hit(damage)
    } else {
      0.0
    }
  }
  
  def target = inRange.headOption
  
  def inRange: Vector[Enemy] = {
    def dist(e: Enemy) = level.path.lengthUntilNode(e.cn - 1) + (level.path(e.cn - 1) - e.pos).r
    level.currentWave.getEnemies.filter(e => isInRange(e) && e.alive && !e.isAtEndOfPath).sortBy(e => -dist(e))
  }
  
  def isInRange(e: Enemy): Boolean = (e.pos - this.pos).r <= this.range
  
  def x = this.pos.x.toFloat
  def y = this.pos.y.toFloat
  def r = this.range.toFloat
  def theta = (lastTarget - this.pos).theta.toFloat
  
}