package core

case class Wave(private var enemies: Vector[Enemy]) {
  def addEnemy(enemy: Enemy) = enemies ++= Vector(enemy)
  def addEnemy(enemy: Enemy*) = enemies ++= enemy
  
  def getEnemies = this.enemies
  
  def setPaths(path: Path) = enemies.map(_.setPath(path))
  def setPositions(pos: Vector2D) = enemies.map(_.pos = pos) 
  
  def isDone = enemies.forall(e => e.dead || e.isAtEndOfPath)
}