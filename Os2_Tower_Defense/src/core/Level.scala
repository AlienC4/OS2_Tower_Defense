package core


case class Level(path: Path) {
  private var waves: Vector[Wave] = Vector()
  
  def addWave(wave: Wave) = this.waves ++= Vector(wave)
  
  def getWave(n: Int) = waves(n)
  
  
}