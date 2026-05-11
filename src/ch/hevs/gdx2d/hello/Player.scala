package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.hello.Platform
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Player(var x: Float, var y:Float) extends Entity {
  var width = 40f
  var height = 40f
  var vy = 0f
  var vx = 0f

  val speed = 400f
  val gravity = -1000f
  val jumpForce = 800f
  var onGround = false

  override def update(dt: Float, platforms: List[Platform]): Unit = {

    vy += gravity * dt
    x += vx *dt
    y += vy * dt

    platforms.foreach { P =>
      val marioLeft = x
      val marioRiht= x + width
      val marioBottom = y
      val marioTop = y + height

      val platformLeft = P.x
      val platformRight = P.x + P.width
      val platformBottom = P.y
      val platformTop = P.y + P.height

      if(marioRiht > platformLeft &&
         marioLeft < platformRight &&
         marioBottom < platformTop &&
         marioTop > platformBottom) {

        if(vy < 0) {
          y = platformTop
          vy = 0
          onGround = true
        }
      }
    }



  }

  override def draw(g: GdxGraphics): Unit = {
    g.setColor(Color.BLUE)
    g.drawFilledRectangle(x + width/2,y + height/2 ,width,height,0)

  }
}
