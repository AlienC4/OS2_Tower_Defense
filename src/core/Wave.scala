package core

case class Wave(private var enemies: Vector[Enemy]) {
  /** Adds a single enemy to this wave */
  def addEnemy(enemy: Enemy) = enemies ++= Vector(enemy)
  /** Adds multiple enemies to this wave */
  def addEnemy(enemy: Enemy*) = enemies ++= enemy
  
  /** Returns the enemies contained in this wave */
  def getEnemies = this.enemies
  /** Returns the enemy at index n */
  def getEnemy(n: Int) = this.enemies(n)
  
  /** Sets the paths for all the enemies in this wave at once */
  def setPaths(path: Path) = enemies.foreach(_.setPath(path))
  /** Sets the positions for all the enemies in this wave at once */
  def setPositions(pos: Vector2D) = enemies.foreach(e => e.setPos(pos)) 
  
  /** Checks if this wave is done */
  def isDone = enemies.forall(e => e.dead || e.isAtEndOfPath)
  
  /** Returns the enemy at index n */
  def apply(n: Int) = this.enemies(n)
  /** Returns the amount of enemies in this wave */
  def size = this.enemies.size
  /** Returns the amount of enemies in this wave */
  def length = this.enemies.length
}
