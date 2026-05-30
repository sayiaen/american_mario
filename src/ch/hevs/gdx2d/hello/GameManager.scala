package ch.hevs.gdx2d.hello

object GameManager {
  var totalScore: Int  = 0
  var RPG: Int = 0
  var GrenadeLauncheraquired: Boolean = false
  var selectmarioskin: Int = 0
  var selectbossskin: Int =0


  def scoreCalculator(amount: Int): Unit={
    totalScore += amount
    println(s"total Score: $totalScore")

  }

  def resetgame(): Unit ={
    totalScore = 0
    RPG = 0
    selectmarioskin = 0
    selectbossskin = 0
  }
}
