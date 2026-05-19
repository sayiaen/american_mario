package ch.hevs.gdx2d.hello

object GameManager {
  var totalScore: Int  = 0
  var RPG: Int = 0
  var GrenadeLauncheraquired: Boolean = false

  def scoreCalculator(amount: Int): Unit={
    totalScore += amount
    println(s"level's passed, total Score: $totalScore")

  }

}
