package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.{Gdx, Input}


class AmericanMario extends PortableApplication(1920, 1080) {
  var player: Player =_

  override def onInit(): Unit = {
    setTitle("mario-phase1")
    player = new Player(200, 500)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.WHITE)
    val dt = Gdx.graphics.getDeltaTime

    player.vx = 0
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.vx = player.speed
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.vx = -player.speed

    // Jumping: Only if the player is touching the ground
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.onGround) {
      player.vy = player.jumpForce
      player.onGround = false // Mario is now in the air
    }
    player.update(dt)
    player.draw(g)

  }
}
object AmericanMario {
  def main(args: Array[String]): Unit = {
    new AmericanMario()
  }
}




