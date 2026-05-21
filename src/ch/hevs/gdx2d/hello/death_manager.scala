package ch.hevs.gdx2d.mygame
import ch.hevs.gdx2d.hello.{Bullet,RPG, GameManager, LootBox,  enemies}
import com.badlogic.gdx.graphics.Color
import ch.hevs.gdx2d.lib.GdxGraphics
import scala.collection.mutable.ListBuffer

case class ExplosionParticle(x: Float, y: Float, radius: Float, var timer: Float)

object death_manager {
  val Lava_level = 50f
  val enemieslist = ListBuffer[enemies]()
  val bulletList = ListBuffer[Bullet]()
  val lootBoxList = ListBuffer[LootBox]()
  val grenadeList = ListBuffer[RPG]()

  val explosionvisual = ListBuffer[ExplosionParticle]()


  def init(spawnedEnemies: List[enemies], spawnedBoxes: List[LootBox]): Unit = {
    enemieslist.clear()
    bulletList.clear()
    lootBoxList.clear()
    grenadeList.clear()
    explosionvisual.clear()


    enemieslist ++= spawnedEnemies
    lootBoxList ++= spawnedBoxes
  }

  def spawnBullet(startX: Float, startY: Float, vx: Float, vy: Float): Unit = {
    bulletList += new Bullet(startX, startY, vx, vy)
  }
  def spawnRPG(StartX:Float, StartY:Float, vx:Float, vy:Float): Unit ={
    grenadeList += new RPG(StartX, StartY, vx, vy)
  }


  def checkStatus(player: Player, dt: Float, camX: Float, platforms: List[Platform]): Boolean = {
    player.updateinvincibilty(dt)

    if (player.y < Lava_level) {
      player.health = 0
      return true
    }

    explosionvisual.foreach(_.timer -= dt)
    explosionvisual.filterInPlace(_.timer >0)

    val deadEnemies = ListBuffer[enemies]()
    val deadBullet = ListBuffer[Bullet]()
    val usedLootBoxes = ListBuffer[LootBox]()
    val explodedRPG = ListBuffer[RPG]()

    for(g <- grenadeList){
      g.update(dt,platforms)

      if(g.explode){
        explodedRPG += g

        val explosionRadius = 175
        val explosionDamage = 20
        val gCenterX =g.x + g.width /2
        val gCenterY  = g.y + g.height/2

        explosionvisual += ExplosionParticle(gCenterX,gCenterY, explosionRadius, 0.15f)
        for (e <- enemieslist if !deadEnemies.contains(e)) {
          val eCenterX = e.x + e.width / 2
          val eCenterY = e.y + e.height / 2
          val distance = math.hypot(eCenterX - gCenterX, eCenterY - gCenterY)

          if (distance <= explosionRadius) {
            if (e.takeDamage(explosionDamage)) {
              deadEnemies += e
              GameManager.scoreCalculator(50)
            }
          }
        }

        // Blast damage loop for Loot Boxes
        for (lb <- lootBoxList if !usedLootBoxes.contains(lb)) {
          val lbCenterX = lb.x + lb.width / 2
          val lbCenterY = lb.y + lb.height / 2
          val distance = math.hypot(lbCenterX - gCenterX, lbCenterY - gCenterY)

          if (distance <= explosionRadius) {
            if (lb.takeDamage(explosionDamage)) {
              usedLootBoxes += lb
              GameManager.scoreCalculator(100)
              GameManager.RPG += 3
              player.health = math.min(5, player.health + 1)
            }
          }
        }
      }
    }

    grenadeList --= explodedRPG



    for (e <- enemieslist) {
      e.update(dt, platforms)

      if (e.KillChck(player)) {
        val isfalling = player.vy < 0
        val mariofeet = player.y
        val enemyhead = e.y + e.height

        if (isfalling && mariofeet >= enemyhead - 15f) {
          player.vy = 600f // Bounceback after the stomp

          if (e.takeDamage(2)) deadEnemies += e
          println("Stomped")
          GameManager.scoreCalculator(50)
        } else {
          player.takedamage(1)
          val knockback  = if (player.x < e.x) -1 else 1
            player.vx = knockback * 300f
            player.vy = 400f //sideways knockback

        }
      }
    }

    for (lb <- lootBoxList) {
      if (!usedLootBoxes.contains(lb)) {
        if (player.getbounds.overlaps(lb.getbounds)) {
          val isMovingUpward = player.vy > 0
          val marioHead = player.y + player.height
          val boxBottom = lb.y


          if (isMovingUpward && marioHead >= boxBottom - 15f && player.y < boxBottom) { //mario top and box bottom collision
            player.vy = -150f
            usedLootBoxes += lb
            println("LOOT!")
            GameManager.scoreCalculator(100)
            GameManager.RPG += 3
            player.health = math.min(5, player.health + 1)
          }
        }
      }
    }


      for (b <- bulletList) {
        b.update(dt)
        if (b.x < camX || b.x > camX + 1920) {
          deadBullet += b
        }
        for (p <- platforms) {
          if (b.hitPlatform(p)) {
            deadBullet += b
          }
        }
        for (lb <- lootBoxList) {
          if (b.x < lb.x + lb.width && b.x + b.width > lb.x &&
            b.y < lb.y + lb.height && b.y + b.height > lb.y) {
            deadBullet += b

            if (lb.takeDamage(b.damage)) {
              usedLootBoxes += lb
              GameManager.scoreCalculator(100) // Grant points when broken
              GameManager.RPG += 3 // Grant ammo
              player.health = math.min(5, player.health + 1)
            }
          }
        }


        for (e <- enemieslist) {
          if (!deadBullet.contains(b) && !deadEnemies.contains(e))
            if (b.hit(e)) {
              deadBullet += b
              if (e.takeDamage(b.damage)) {
                deadEnemies += e
                GameManager.scoreCalculator(50)


              }
            }
        }
      }
    enemieslist --= deadEnemies
    bulletList --= deadBullet
    lootBoxList --= usedLootBoxes

    player.health <= 0
  }

    def draw(g: GdxGraphics, camX: Float, player: Player): Unit = {
      g.setColor(Color.ORANGE)
      g.drawFilledRectangle(camX + 800, Lava_level / 2, 1920, Lava_level, 0)
      //red/orange lava /color is not showing/fixed it was an order issue

      enemieslist.foreach((_.draw(g)))
      lootBoxList.foreach(_.draw(g))
      bulletList.foreach(_.draw(g))
      grenadeList.foreach(_.draw(g))

      g.setColor(Color.RED)
      for(particle <- explosionvisual){
        g.drawCircle(particle.x, particle.y, particle.radius)
      }

      g.setColor(Color.BLACK)
      g.drawString(camX + 90, 1020, s"SCORE: ${GameManager.totalScore}", 36)
      g.drawString(camX + 90, 980, s"GRENADE: ${GameManager.RPG}", 36)
      g.drawString(camX + 90, 940, s"HEALTH: ${player.health}", 36)


    }

}


