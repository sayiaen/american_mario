package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.mygame.Platform
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Player(var x: Float, var y:Float) extends Entity {
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

  def takedamage(amount: Int): Unit = {
    if(invinnciblityttimer <= 0){
      health -= amount
      invinnciblityttimer = damageCooldown
      println(s"Took damage! remaining health: $health")
    }
  }

  def updateinvincibilty(dt: Float): Unit = {
    if (invinnciblityttimer > 0) invinnciblityttimer -= dt
  }

  def collidesWith(P: Platform) : Boolean = {
      val marioLeft = x
      val marioRiht= x + width
      val marioBottom = y
      val marioTop = y + height

      val platformLeft = P.x
      val platformRight = P.x + P.width
      val platformBottom = P.y
      val platformTop = P.y + P.height

        marioRiht > platformLeft &&
        marioLeft < platformRight &&
        marioBottom < platformTop &&
        marioTop > platformBottom // error on contact with the end goal, if fixed direction key failure


  }

  override def update(dt: Float, platforms: List[Platform]): Unit = {


    x += vx *dt
    for(p <- platforms){
      if(collidesWith(p)) {
        if(vx > 0) x = p.x -width
        else if(vx<0) x =p.x + p.width
        vx = 0
      }
    }

    vy += gravity * dt
    y += vy * dt
    onGround = false

    for(p <- platforms){
      if(collidesWith(p)){
        if(vy < 0) {
          y =p.y + p.height
          vy = 0
          onGround = true
        } else if (vy > 0){
          y = p.y - height
          vy = 0

        }
      }
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.setColor(Color.BLUE)
    g.drawFilledRectangle(x + width/2, y + height/2 , width, height,0)

  }
}
