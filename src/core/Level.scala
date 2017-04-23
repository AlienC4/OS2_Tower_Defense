package core


case class Level(path: Path) {
  private var waves: Vector[Wave] = Vector()
  private var c = 0
  
  def addWave(wave: Wave) = this.waves ++= Vector(wave)
  def addWave(wave: Wave*) = this.waves ++= wave
  
  def hasNext = c >= 0 && c < waves.size
  def currentWave = getWave(c)
  def nextWave = c += 1
  
  def isDone = waves.forall(w => w.isDone)
  
  def getWave(n: Int) = waves(n)
  def apply(n: Int) = getWave(n)
  
}
