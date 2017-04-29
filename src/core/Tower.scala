package core


/**
 * @param pos: The position of the tower
 * @param cost: The cost to place or upgrade the tower
 * @param range: The attack range of the tower
 * @param damage: The attack damage of the tower
 * @param rof: The rate of fire of the tower, in shots per second
 */
case class Tower(pos: Vector2D, var cost: Int, var range: Double, var damage: Double, var rof: Double) {
  
  private var lastShot = System.currentTimeMillis() // Initially the time when the tower was created
  var lastTarget: Vector2D = Vector2D(0, 0)
  /** Time between shots, in milliseconds */
  private def tbs = 1 / rof * 1000
  var level: Level = null
  var upLevel = 1
  
  /** Upgrades this tower */
  def upgrade = {
    this.upLevel += 1
    this.range *= 1.1
    this.damage *= 1.4
    this.rof += 0.05
    this.cost = (1.25 * this.cost).toInt
  }
  
  /** Shoots at the current target if enough time has passed */
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
  
  /** Current target */
  def target = inRange.headOption
  
  /** Gets all the enemies that are currently in range and sorts them by the distance travelled from the start of the path */
  private def inRange: Vector[Enemy] = {
    def dist(e: Enemy) = level.path.lengthUntilNode(e.cn - 1) + (level.path(e.cn - 1) - e.pos).r
    level.currentWave.getEnemies.filter(e => isInRange(e) && e.alive && !e.isAtEndOfPath).sortBy(e => -dist(e))
  }
  
  /** Checks whether the enemy given as parameter is in range on this */
  private def isInRange(e: Enemy): Boolean = e.pos.isWithin(this.range, this.pos)
  
  /** The x coordinate of this tower's position as float */
  def x = this.pos.x.toFloat
  /** The y coordinate of this tower's position as float */
  def y = this.pos.y.toFloat
  /** The range of this tower as float */
  def r = this.range.toFloat
  /** The facing angle of this tower as float */
  def theta = (lastTarget - this.pos).theta.toFloat
  
}