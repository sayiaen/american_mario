package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Entity
import com.badlogic.gdx.graphics.Color

class Platform(var x: Float, var y: Float, var width: Float, var height: Float) extends Entity{

  override def update(dt: Float, platforms: List[Platform]): Unit = {}

  override def draw(g: GdxGraphics): Unit = {
    g.setColor(Color.BLACK)
    g.drawFilledRectangle(x + width/2, y + height/2, width, height, 0)
  }
}
