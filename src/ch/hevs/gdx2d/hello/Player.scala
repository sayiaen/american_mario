package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Player(var x: Float, var y:Float) extends Entity {
  var width = 40f
  var height = 40f
  var vy = 0f
  var vx = 0f

  val speed = 400f
  val gravity = 1000f
  val jumpForce = -800f
  var onGround = false

  override def update(dt: Float): Unit = {
    vy += gravity * dt

    x += vx *dt
    y += vy * dt

    if(y < 100){
      y = 100
      vy = 0
      onGround = true
    }

  }

  override def draw(g: GdxGraphics): Unit = {
    g.setColor(Color.BLUE)
    g.drawFilledRectangle(x,y,width,height,0)

  }
}
