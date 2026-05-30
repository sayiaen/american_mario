package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.mygame.Platform
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Intersector, Rectangle}
import ch.hevs.gdx2d.hello.{Assets, GameManager}
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Player(var x: Float, var y: Float) extends Entity {
  var health = 5
  var invinnciblityttimer = 0f
  var damageCooldown = 1.5f

  var width = 60f
  var height = 60f
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
      println(s"remaining health: $health")
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

  override def update(dt: Float, platforms: List[Platform], camX: Float): Unit = {
    updateinvincibilty(dt)

    // 1. MOVEMENT INPUT CONTROLS
    vx = 0f
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      vx = speed
      FacingDirection = 1f
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      vx = -speed
      FacingDirection = -1f
    }

    // JUMP CONTROL
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && onGround) {
      vy = jumpForce
      onGround = false
    }

    // 2. COMBAT SHOOTING CONTROLS
    // 'F' for standard horizontal bullets
    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
      val bulletSpeed = 600f * FacingDirection
      death_manager.spawnBullet(x + width / 2, y + height / 2, bulletSpeed, 0f)
    }

    // 'G' for ballistic arcing RPG missiles
    if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
      if (GameManager.RPG > 0) {
        GameManager.RPG -= 1 // Deduct 1 ammo stock reserve

        val launchVx = 350f * FacingDirection
        val launchVy = 450f // Initial upward push vector

        death_manager.spawnRPG(x + width / 2, y + height, launchVx, launchVy)
      } else {
        println("OUT OF RPG AMMO! Smash green LootBoxes upwards to restock.")
      }
    }

    // 3. PHYSICS ENGINE & MOVEMENT INTEGRATION
    x += vx * dt
    vy += gravity * dt
    y += vy * dt
    onGround = false

    val playerRect = new Rectangle(x, y, width, height)
    val mtv = new Intersector.MinimumTranslationVector()

    for (p <- platforms) {
      val platformRect = new Rectangle(p.x, p.y, p.width, p.height)

      if (Intersector.overlapConvexPolygons(rectToPolygon(playerRect), rectToPolygon(platformRect), mtv)) {
        // Push player out by minimum translation vector values
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

  def rectToPolygon(r: Rectangle): com.badlogic.gdx.math.Polygon = {
    new com.badlogic.gdx.math.Polygon(Array(
      r.x,          r.y,
      r.x + r.width, r.y,
      r.x + r.width, r.y + r.height,
      r.x,          r.y + r.height
    ))
  }

  override def draw(g: GdxGraphics): Unit = {
    val region = new TextureRegion(Assets.playerTex)
    if(FacingDirection < 0){
      region.flip(true, false)
    }
    g.draw(region, x, y, width, height)
     //g.setColor(Color.BLUE)
     //   g.drawFilledRectangle(x + width / 2, y + height / 2, width, height, 0)
  }
}