package ch.hevs.gdx2d.hello
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.{Player, Platform}

abstract class enemies(var x: Float, var y: Float, val width: Float, val height: Float, val maxHealth: Int = 1) {
  var health: Int = maxHealth

  def draw(g: GdxGraphics): Unit

  def update(dt: Float, platforms: List[Platform]): Unit

  def KillChck(player: Player): Boolean = {
    player.x < x + width &&
      player.x + player.width > x &&
      player.y < y + height &&
      player.y + player.height > y

  }

  def takeDamage(amount: Int): Boolean = {
    health -= amount
    health <= 0
  }
}







