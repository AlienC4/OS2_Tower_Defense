package core

case class Player(var money: Int, var lives: Int) {
  private val initMoney = money
  private val initLives = lives
  
  def reload() = {
    money = initMoney
    lives = initLives
  }
}