package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.mygame.Platform
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.{Gdx, Input}


class AmericanMario extends PortableApplication(1920, 1080) {
  var player: Player =_
  var platforms = List[Platform]()

  override def onInit(): Unit = {
    setTitle("mario-phase1")
   platforms = List(
      new Platform(0,500,1000,100),//starting point
      new Platform(1200, 700, 100, 100),
      new Platform(1600, 700, 100, 100),
      new Platform(2000,700,100,100),
      new Platform(2400,800,600,100),
      new Platform(3500, 1000, 500, 50),
      new Platform(4200,1000,400,100,true),

    )
   player = new Player(100, 700)

  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    val dt = Gdx.graphics.getDeltaTime

    player.vx = 0
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.vx = player.speed
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.vx = -player.speed

    // Jumping: Only if the player is touching the ground
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.onGround) {
      player.vy = player.jumpForce
      player.onGround = false
    }// Mario is now in the air

      player.update(dt, platforms)

      g.clear(Color.WHITE)

      val camX = if(player.x < 960) 0f else (player.x -960)
      //val camY = 540f
      g.moveCamera(camX, 540)

      platforms.foreach(p => p.draw(g))
      player.draw(g)

      if (player.y < 0) {
        println("Game Over")
        onInit()
      }

      platforms.foreach(p =>
        if (p.isGoal) {
          if (player.collidesWith(p)) {
            g.setColor(Color.RED)
            g.drawString(camX +500, 600, "LEVEL COMPLETE, PRESS SPACE TO RESTART", 30)

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
              onInit()
            }
            println("Level Complete")

          }
        }
      )






      //println(s"Mario is at: ${player.x}, ${player.y}")//gemini suggested so we can see what's happening would be useful for debugging

      g.drawFPS()

    }


}


 object AmericanMario {
   def main(args: Array[String]): Unit = {
     new AmericanMario()
          }
}





