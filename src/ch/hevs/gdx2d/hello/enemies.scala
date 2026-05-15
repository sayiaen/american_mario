package ch.hevs.gdx2d.hello
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Player

abstract class enemies (var x: Float, var y: Float, val width: Float, val height: Float) {
  def draw(g: GdxGraphics): Unit

  def update(dt: Float): Unit

  def KillChck(player: Player): Boolean = {
    player.x < x + width &&
      player.x + player.width > x &&
      player.y < y + height &&
      player.y + player.height > y

  }
}







