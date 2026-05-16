package ch.hevs.gdx2d.mygame
import ch.hevs.gdx2d.hello.{Minion, enemies}
import com.badlogic.gdx.graphics.Color
import ch.hevs.gdx2d.lib.GdxGraphics

import scala.collection.mutable.ListBuffer

object death_manager {
  val Lava_level = 50f
  val enemies = ListBuffer[enemies]()

  def init(): Unit = {
    enemies.clear()
    enemies += new Minion(900, 300, 200)
    enemies += new Minion(2200, 400, 300)
  }

  def checkStatus(player: Player, dt: Float): Boolean = {
    player.updateinvincibilty(dt)


    if (player.y < Lava_level) {
      player.health = 0
      return true
    }

    val deadEnemies = ListBuffer[enemies]()

    for (e <- enemies) {
      e.update(dt)

      if (e.KillChck(player)) {
        val isfalling = player.vy < 0
        val mariofeet = player.y
        val enemyhead = e.y + e.height

        if (isfalling && mariofeet >= enemyhead - 15f) {
          player.vy = 200f // Bounceback after the stomp
          val isDead = e.takeDamage(2)
          if (isDead) deadEnemies += e
          println("Stomped")
        } else {
          player.takedamage(1)
        }
      }
    }
    enemies --= deadEnemies
    player.health <= 0
  }

    def draw(g: GdxGraphics, camX: Float, player: Player): Unit = {
      g.setColor(Color.ORANGE)
      g.drawFilledRectangle(camX + 800, Lava_level / 2, 1920, Lava_level, 0)
      //red/orange lava /color is not showing/fixed it was an order issue

      g.setColor(Color.WHITE)
      g.drawString(camX + 800, 75, "Danger: GET BURNED", 40)

      g.setColor(Color.RED)
      g.drawString(camX + 50, 1000, s"HEALTH: ${"<3" * player.health}", 30)

      enemies.foreach((_.draw(g)))
    }
  }
