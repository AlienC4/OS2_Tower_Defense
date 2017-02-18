package core

import scala.math._

case class Vector2D(x: Double, y: Double) {
  
  def r = hypot(x, y)
  def theta = atan2(y, x)
  
  def +(v: Vector2D) = Vector2D(x + v.x, y + v.y)
  def *(m: Double) = m * this
  
  def unit = (1/r) * this
  
  
  implicit class mult(m: Double) {
     def ^(a: Double) = pow(m, a)
     def *(v: Vector2D): Vector2D = Vector2D(m * v.x, m * v.y)
  }
}

