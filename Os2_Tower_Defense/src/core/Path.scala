package core

case class Path(private var path: Vector[Coords]) {
  
  def addNode(c: Coords) = this.path ++= Vector(c)
  
  def getPath = this.path
  
}