package core

case class Wave(private var enemies: Vector[Enemy]) {
  def addEnemy(enemy: Enemy) = enemies ++= Vector(enemy)
  def addEnemy(enemy: Enemy*) = enemies ++= enemy
  
  def getEnemies = this.enemies
  def getEnemy(n: Int) = this.enemies(n)
  
  
  def setPaths(path: Path) = enemies.map(_.setPath(path))
  def setPositions(pos: Vector2D) = enemies.foreach(e => e.setPos(pos)) 
  
  def isDone = enemies.forall(e => e.dead || e.isAtEndOfPath)
  
  def apply(n: Int) = this.enemies(n)
  def size = this.enemies.size
  def length = this.enemies.length
}