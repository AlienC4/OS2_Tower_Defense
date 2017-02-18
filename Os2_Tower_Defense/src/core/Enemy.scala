package core

case class Enemy(var health: Double, var speed: Vector2D, var pos: Vector2D) {
  
  var path: Path = null
  var currentNode: Int = 0
  
  def alive = this.health > 0
  def dead = !alive
  
  def hit(damage: Double) = health -= damage
  
}

trait Flying {
  val flying = true
}