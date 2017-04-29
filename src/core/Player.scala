package core

case class Player(var money: Int, var lives: Int) {
  
  def reload(level: Level) = {
    money = level.money
    lives = level.lives
  }
}