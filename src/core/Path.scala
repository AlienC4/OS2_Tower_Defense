package core

case class Path(private var path: Vector[Vector2D]) {
  
  def addNode(c: Vector2D) = this.path ++= Vector(c)
  def addNode(c: Vector2D*) = this.path ++= c
  
  def apply(n: Int) = if (n < 0) path(0) else path(n)
  
  def getPath = this.path
  
  def lengthUntilNode(n: Int): Double = {
    if (n <= 0) 0.0
    else if (n >= path.length) lengthUntilNode(path.length - 1)
    else (path(n) - path(n - 1)).r + lengthUntilNode(n - 1)
  }
  
  def length = this.path.length
  
  override def toString = this.path.toString()
  
}