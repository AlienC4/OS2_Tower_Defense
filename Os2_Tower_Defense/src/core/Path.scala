package core

case class Path(private var path: Vector[Vector2D]) {
  
  def addNode(c: Vector2D) = this.path ++= Vector(c)
  def addNode(c: Vector2D*) = this.path ++= c
  
  def apply(n: Int) = path(n)
  
  def getPath = this.path
  
  def length = this.path.length
  
}