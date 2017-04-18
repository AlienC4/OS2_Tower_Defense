package GUI

import core._
import scala.swing._


object Main extends SimpleSwingApplication {
  
  val width = 1000
  val height = 800
  val fullHeight = 810
  val squareSize = 20
  val colCount = width / squareSize
  val rowCount = height / squareSize
  private var cells: List[Rectangle] = List()
  
  
  def top = new MainFrame {
    
    title = "Super Awesome Tower Defense"
    resizable = true
    
    minimumSize = new Dimension(width, fullHeight)
    preferredSize = new Dimension(width, fullHeight)
    maximumSize = new Dimension(width, fullHeight)
    
    val grid = 5
    
    
  }
  
  
}