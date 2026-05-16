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

    death_manager.init()
   platforms = List(
      new Platform(0,100,1000,100),//starting point
      new Platform(1200, 300, 100, 100),
      new Platform(1600, 300, 100, 100),
      new Platform(2000,300,100,100),
      new Platform(2400,400,600,100),
      new Platform(3500, 6000, 500, 50),
      new Platform(4200,6000,400,100,true),

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
      //val camY = math.max(540f, player.y)//ask how to have a camera both follows horizontal and diagonal
      g.moveCamera(camX, 50)

      platforms.foreach(p => p.draw(g))



      if(death_manager.checkStatus(player, dt)) {
        println("DEATH")
        println("Game Over")
        onInit()
      }

      death_manager.draw(g, camX, player)
      player.draw(g)
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





