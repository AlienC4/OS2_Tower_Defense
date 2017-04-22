package core

import scala.math._

case class Vector2D(x: Double, y: Double) {
  
  /** Gives the length of this vector */
  def r = hypot(x, y)
  def length(v: Vector2D) = v.r
  /** Gives the signed angle between this vector and the x-axis in degrees*/
  def theta = atan2(y, x).toDegrees
  def angle(v: Vector2D) = v.theta
  
  /** Adds two Vectors together */
  def +(v: Vector2D) = Vector2D(x + v.x, y + v.y)
  def +(xy : (Double, Double)) = Vector2D(x + xy._1, y + xy._2)
  /** Substacts the given Vector from this */
  def -(v: Vector2D) = Vector2D(x - v.x, y - v.y)
  /** Multiplies the components of this vector by a given value, effectively strecting the vector */
  def *(m: Double) = m * this
  /** Divides the components of this vector by a given value, effectively shrinking the vector */
  def /(m: Double) = Vector2D(x / m, y / m)
  
  /** Gives a vector that has the same heading as this but has a length of 1 */
  def unit = this / r
  def normalize(v: Vector2D) = v.unit
  
  def offset(ox: Double, oy: Double) = this + Vector2D(ox, oy)
  
  
  implicit class mult(m: Double) {
     def ^(a: Double) = pow(m, a)
     def *(v: Vector2D): Vector2D = Vector2D(m * v.x, m * v.y)
  }
}

