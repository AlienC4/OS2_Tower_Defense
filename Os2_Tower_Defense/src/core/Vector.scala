package core

import scala.math._

case class Vector(x: Double, y: Double) {
  
  def r = hypot(x, y)
  def theta = atan2(y, x)
  
  def +(v: Vector) = Vector(x + v.x, y + v.y)
  
  implicit class mult(m: Double) {
     def ^(a: Double) = pow(m, a)
     def *(v: Vector) = Vector(m * x, m * y)
  }
}



