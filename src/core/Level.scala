package core


case class Level(number: Int, money: Int, lives: Int, path: Path) {
  private var waves: Vector[Wave] = Vector()
  private var c = 0
  
  def addWave(wave: Wave) = this.waves ++= Vector(wave)
  def addWave(wave: Wave*) = this.waves ++= wave
  
  def hasNext = c >= 0 && c < waves.size - 1
  def currentWave = getWave(c)
  def nextWave = c += 1
  
  def isDone = waves.forall(w => w.isDone)
  
  def getWave(n: Int) = waves(n)
  def apply(n: Int) = getWave(n)
  
  def reload() = {
    val l = loadLevel(number)
    waves = l.waves
    c = 0
  }
  
}
