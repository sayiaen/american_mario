package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.hello.Platform
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.{Gdx, Input}


class AmericanMario extends PortableApplication(1920, 1080) {
  var player: Player =_
  var platforms = List[Platform]()

  override def onInit(): Unit = {
    setTitle("mario-phase1")
    player = new Player(200, 500)
    platforms = List(
      new Platform(0,50,1920,100),
      new Platform(500,400,300,40)
    )

  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.WHITE)
    platforms.foreach(p => p.draw(g))
    player.draw(g)

    val dt = Gdx.graphics.getDeltaTime

    player.vx = 0
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.vx = player.speed
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.vx = -player.speed

    // Jumping: Only if the player is touching the ground
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.onGround) {
      player.vy = player.jumpForce
      player.onGround = false // Mario is now in the air
    }
    player.update(dt, platforms)



  }
}
object AmericanMario {
  def main(args: Array[String]): Unit = {
    new AmericanMario()
  }
}




