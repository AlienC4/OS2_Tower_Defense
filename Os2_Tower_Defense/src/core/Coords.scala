package core

case class Coords(x: Double, y: Double) {
  implicit def toVector = Vector2D(x, y)
}