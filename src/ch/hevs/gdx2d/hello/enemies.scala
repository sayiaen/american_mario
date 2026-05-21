package ch.hevs.gdx2d.hello
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.{Player, Platform}

abstract class enemies(var x: Float, var y: Float, var width: Float, var height: Float, var maxHealth: Int = 1) {
  var health: Int = maxHealth
  var isDead = false

  def draw(g: GdxGraphics): Unit

  def update(dt: Float, platforms: List[Platform]): Unit

  def canShoot: Boolean = false
  def shoot(): Unit={}


  def KillChck(player: Player): Boolean = {
    player.x < x + width &&
      player.x + player.width > x &&
      player.y < y + height &&
      player.y + player.height > y

  }

  def takeDamage(D_amount: Int): Boolean = {
    health -= D_amount
    if(health <= 0) isDead = true
    isDead
  }
}







