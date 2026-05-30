package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.hello.Assets
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Entity
import com.badlogic.gdx.graphics.Color

class Platform(var x: Float, var y: Float, var width: Float, var height: Float, var isGoal: Boolean = false) extends Entity{

  override def update(dt: Float, platforms: List[Platform], camX: Float): Unit = {}

  override def draw(g: GdxGraphics): Unit = {
    if (isGoal){
      g.setColor(Color.GOLD)

    g.drawFilledRectangle(x + width / 2, y + height / 2, width, height, 0)}
    else
    {
      val tilesize = height
      var currentX = x
      while (currentX < x + width) {
        val currenttileWidth = math.min(tilesize, (x + width) - currentX)
        g.draw(Assets.platformTex, currentX, y, currenttileWidth, height)
        currentX += tilesize
      }
    }
  }
}
