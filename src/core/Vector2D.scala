package core

import scala.math._

case class Vector2D(x: Double, y: Double) {
  
  /** Gives the length of this vector */
  def r = hypot(x, y)
  /** Gives the signed angle between this vector and the x-axis in radians*/
  def theta = atan2(y, x)
  
  /** Adds two Vectors together */
  def +(v: Vector2D) = Vector2D(x + v.x, y + v.y)
  /** Adds a tuple to this vector */
  def +(xy : (Double, Double)) = Vector2D(x + xy._1, y + xy._2)
  /** Substacts the given Vector from this */
  def -(v: Vector2D) = Vector2D(x - v.x, y - v.y)
  /** Multiplies the components of this vector by a given value, effectively stretching the vector */
  def *(m: Double) = m * this
  /** Divides the components of this vector by a given value, effectively shrinking the vector */
  def /(m: Double) = Vector2D(x / m, y / m)
  
  /** Gives a vector that has the same heading as this but has a length of 1 */
  def unit = this / r
  
  /** Returns a vector that is this vector offset by the value specified by ox and oy */
  def offset(ox: Double, oy: Double) = this + Vector2D(ox, oy)
  
  /** Checks if the difference between this and that is smaller than radius */
  def isWithin(radius: Double, that: Vector2D) = (this - that).r <= radius
  
  
  implicit class mult(m: Double) {
    /** Allows for writing vector-scalar multiplication both ways: m * v and v * m, where v is a vector and m is a number */
     def *(v: Vector2D): Vector2D = Vector2D(m * v.x, m * v.y)
  }
}

