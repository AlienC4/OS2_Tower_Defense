package core

case class Wave(private var enemies: Vector[Enemy]) {
  def addEnemy(enemy: Enemy) = enemies ++= Vector(enemy)
  def addEnemy(enemy: Enemy*) = enemies ++= enemy.toVector
  
  def setPaths(path: Path) = enemies.map(_.setPath(path))
  
  def isDone = enemies.forall(_.dead)
}