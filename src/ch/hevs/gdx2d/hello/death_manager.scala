package ch.hevs.gdx2d.mygame
import ch.hevs.gdx2d.hello.{Bullet, Minion, enemies}
import com.badlogic.gdx.graphics.Color
import ch.hevs.gdx2d.lib.GdxGraphics
import org.graalvm.nativeimage.Platforms

import scala.collection.mutable.ListBuffer

object death_manager {
  val Lava_level = 50f
  val enemieslist = ListBuffer[enemies]()
  val bulletList = ListBuffer[Bullet]()


  def init(spawnedEnemies: List[enemies]): Unit = {
    enemieslist.clear()
    bulletList.clear()
    enemieslist ++= spawnedEnemies
  }

  def spawnBullet(startX: Float, startY: Float, vx: Float, vy: Float): Unit ={
    bulletList += new Bullet(startX, startY, vx, vy)
  }




    def checkStatus(player: Player, dt: Float, camX: Float, platforms: List[Platform]): Boolean = {
      player.updateinvincibilty(dt)

      if(player.y < Lava_level) {
        player.health = 0
        return true

    }

    val deadEnemies = ListBuffer[enemies]()
    val deadBullet = ListBuffer[Bullet]()

    for (e <- enemieslist) {
      e.update(dt, platforms)

      if (e.KillChck(player)) {
        val isfalling = player.vy < 0
        val mariofeet = player.y
        val enemyhead = e.y + e.height

        if (isfalling && mariofeet >= enemyhead - 15f) {
          player.vy = 600f // Bounceback after the stomp
          val isDead = e.takeDamage(2)
          if (isDead) deadEnemies += e
          println("Stomped")
        } else {
          player.takedamage(1)
        }
      }
    }

    for(b <- bulletList){
      b.update(dt)

      if(b.x < camX || b.x > camX + 1920){
        deadBullet += b
      }

      for (p <- platforms) {
        if (b.hitPlatform(p)) {
          deadBullet += b
        }
      }

      for (e <- enemieslist) {
        e.update(dt, platforms)
        if (b.hit(e)) {
          deadBullet += b
          if (e.takeDamage(b.damage)) {
            deadEnemies += e
          }
        }
      }
    }
    enemieslist --= deadEnemies
    bulletList --= deadBullet
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

      enemieslist.foreach((_.draw(g)))

      bulletList.foreach(_.draw(g))
    }
  }
