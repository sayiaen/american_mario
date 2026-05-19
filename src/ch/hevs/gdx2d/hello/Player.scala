package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.mygame.Platform
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.Color

class Player(var x: Float, var y: Float) extends Entity {
  var health = 5
  var invinnciblityttimer = 0f
  var damageCooldown = 1.5f

  var width = 40f
  var height = 40f
  var vy = 0f
  var vx = 0f

  val speed = 400f
  val gravity = -1000f
  val jumpForce = 800f
  var onGround = false
  var FacingDirection = 1f

  def takedamage(amount: Int): Unit = {
    if (invinnciblityttimer <= 0) {
      health -= amount
      invinnciblityttimer = damageCooldown
      println(s"Took damage! remaining health: $health")
    }
  }

  def updateinvincibilty(dt: Float): Unit = {
    if (invinnciblityttimer > 0) invinnciblityttimer -= dt
  }

  def collidesWith(P: Platform): Boolean = {
    val marioLeft = x
    val marioRight = x + width
    val marioBottom = y
    val marioTop = y + height

    val platformLeft = P.x
    val platformRight = P.x + P.width
    val platformBottom = P.y
    val platformTop = P.y + P.height

    marioRight >= platformLeft &&
      marioLeft <= platformRight &&
      marioBottom <= platformTop &&
      marioTop >= platformBottom
  }

  import com.badlogic.gdx.math.{Intersector, Rectangle, Vector2}

  override def update(dt: Float, platforms: List[Platform], camX: Float): Unit = {
    updateinvincibilty(dt)

    if (vx > 0) FacingDirection = 1f
    if (vx < 0) FacingDirection = -1f


    if (Gdx.input.justTouched()) {//logic to use the mouse to fire taken from online
      val bulletSpeed = 700f
      val spawnX = x + width / 2
      val spawnY = y + height / 2

      val mouseWorldX = Gdx.input.getX + camX
      val mouseWorldY = 1080f - Gdx.input.getY // Inverts screen layout height

      // Calculate directional distance values
      val deltaX = mouseWorldX - spawnX
      val deltaY = mouseWorldY - spawnY
      val distance = math.sqrt((deltaX * deltaX) + (deltaY * deltaY)).toFloat

      if (distance > 0) {
        val bulletVx = (deltaX / distance) * bulletSpeed
        val bulletVy = (deltaY / distance) * bulletSpeed

        death_manager.spawnBullet(spawnX, spawnY, bulletVx, bulletVy)
      }
    }

    // Move both axes
    x += vx * dt
    vy += gravity * dt
    y += vy * dt
    onGround = false

    val playerRect = new Rectangle(x, y, width, height)
    val mtv = new Intersector.MinimumTranslationVector()

    for (p <- platforms) {
      val platformRect = new Rectangle(p.x, p.y, p.width, p.height)

      if (Intersector.overlapConvexPolygons(rectToPolygon(playerRect), rectToPolygon(platformRect), mtv)) {

        // Push player out by the minimum amount
        x += mtv.normal.x * mtv.depth
        y += mtv.normal.y * mtv.depth


        if (mtv.normal.y > 0.5f) {
          vy = 0
          onGround = true
        }

        else if (mtv.normal.y < -0.5f) {
          vy = 0
        }

        else if (math.abs(mtv.normal.x) > 0.5f) {
          vx = 0
        }


        playerRect.set(x, y, width, height)
      }
    }
  }

  // Helper: convert Rectangle to polygon for Intersector
  def rectToPolygon(r: Rectangle): com.badlogic.gdx.math.Polygon = {
    val poly = new com.badlogic.gdx.math.Polygon(Array(
      r.x,          r.y,
      r.x + r.width, r.y,
      r.x + r.width, r.y + r.height,
      r.x,          r.y + r.height
    ))
    poly
  }//this is the logic used in libgdx2d for collisions i took it from there and modified to mine after a brief research
  override def draw(g: GdxGraphics): Unit = {
    g.setColor(Color.BLUE)
    g.drawFilledRectangle(x + width / 2, y + height / 2, width, height, 0)
  }
}