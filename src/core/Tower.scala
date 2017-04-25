package core


/**
 * @params pos: The position of the tower
 * @param range: The attack range of the tower
 * @param damage: the attack damage of the tower
 * @param rof: The rate of fire of the tower, in shots per second
 */
case class Tower(pos: Vector2D, var range: Double, var damage: Double, var rof: Double) {
  
  private var lastShot = System.currentTimeMillis() // Initially the time when the tower was created
  var lastTarget: Vector2D = Vector2D(0, 0)
  /** Time between shots, in milliseconds */
  private def tbs = 1 / rof * 1000
  var level: Level = null
  var upLevel = 1
  
  
  def upgrade = {
    this.upLevel += 1
    this.range *= 1.2
    this.damage *= 1.1
    this.rof *= 1.05
  }
  
  def shoot = {
    val shot = System.currentTimeMillis()
    if (target.isDefined && target.get.alive && (shot - lastShot) > tbs) {
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
    level.currentWave.getEnemies.filter(e => isInRange(e) && e.alive).sortBy(e => -dist(e))
  }
  
  def isInRange(e: Enemy): Boolean = (e.pos - this.pos).r <= this.range
  
  def x = this.pos.x.toFloat
  def y = this.pos.y.toFloat
  def r = this.range.toFloat
  def theta = (lastTarget - this.pos).theta.toFloat
  
}