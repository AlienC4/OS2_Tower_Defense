package core

case class Coords(x: Double, y: Double) {
  def toVector = Vector2D(x, y)
}