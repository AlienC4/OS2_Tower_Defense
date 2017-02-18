package core

import scala.math._

case class Vector2D(x: Double, y: Double) {
  
  /** Gives the length of this vector */
  def r = hypot(x, y)
  /** Gives the signed angle between this vector and the x-axis */
  def theta = atan2(y, x)
  
  /** Adds two Vectors together */
  def +(v: Vector2D) = Vector2D(x + v.x, y + v.y)
  /** Multiplies the components of this vector by a given value, effectively strecting the vector */
  def *(m: Double) = m * this
  /** Divides the components of this vector by a given value, effectively shrinking the vector */
  def /(m: Double) = Vector2D(x / m, y / m)
  
  /** Gives a vector that has the same heading as this but has a length of 1 */
  def unit = this / r
  
  
  implicit class mult(m: Double) {
     def ^(a: Double) = pow(m, a)
     def *(v: Vector2D): Vector2D = Vector2D(m * v.x, m * v.y)
  }
}

