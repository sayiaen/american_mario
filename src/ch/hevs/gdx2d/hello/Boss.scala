package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Platform
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Intersector, Rectangle, Polygon}

 class Boss ( val StartX: Float, val StartY: Float, val bossArenaleftX: Float, val bossarenaWidth: Float) extends enemies(StartX, StartY, 80f, 80f, 200){
  val maxWidth = 120
  val maxHeight = 120
  val growth  = 10
  var hitsrecieved = 0
  val necessaryhits = 20
  var isvulnerable  = false

  var vy = 0f
  val gravity = -1000
  var direction = -1

  override def takeDamage(D_amount: Int): Boolean = {
    if(!isvulnerable) {
      hitsrecieved += D_amount
      if (hitsrecieved >= necessaryhits) {
        hitsrecieved = 0
        grow()
      }
      false
    }else{
    health -= D_amount
    if(health <= 0) isDead = true
      isDead
    }
  }


  def grow(): Unit = {
    width = math.min(width+growth, maxWidth)
    height  = math.min(height + growth, maxHeight)

    if(width == maxWidth && height == maxHeight){
      isvulnerable = true
      println("phase 2, boss is vulnerable")
    }
  }

  override def update(dt: Float, platforms: List[Platform]): Unit = {
    val speed  = if(!isvulnerable) 80 else 160
    x += speed * direction * dt

    // BOUNDARY EDGE CONTAINMENT: Forces Boss to flip velocity direction before passing edge bounds
    if (x < bossArenaleftX) {
      x = bossArenaleftX
      direction = 1
    } else if (x + width > bossArenaleftX + bossarenaWidth) {
      x = bossArenaleftX + bossarenaWidth - width
      direction = -1
    }
    var bossRect = new Rectangle(x, y, width, height)
    val mtv = new Intersector.MinimumTranslationVector()

    for (p <- platforms) {
      val pr = new Rectangle(p.x, p.y, p.width, p.height)
      if (Intersector.overlapConvexPolygons(rectToPolygon(bossRect), rectToPolygon(pr), mtv)) {
        if (math.abs(mtv.normal.x) > 0.5f) {
          x += mtv.normal.x * mtv.depth
          direction *= -1
        }
        bossRect.set(x, y, width, height)
      }
    }
    vy += gravity * dt
    y  += vy * dt
    bossRect.set(x, y, width, height)

    for (p <- platforms) {
      val pr = new Rectangle(p.x, p.y, p.width, p.height)
      if (Intersector.overlapConvexPolygons(rectToPolygon(bossRect), rectToPolygon(pr), mtv)) {
        if (mtv.normal.y > 0.5f) {
          y  += mtv.normal.y * mtv.depth
          vy  = 0f
        } else if (mtv.normal.y < -0.5f) {
          y  += mtv.normal.y * mtv.depth
          vy  = 0f
        }
        bossRect.set(x, y, width, height)
      }
    }
  }

 def rectToPolygon(r: Rectangle) = new com.badlogic.gdx.math.Polygon(Array(
      r.x,           r.y,
      r.x + r.width, r.y,
      r.x + r.width, r.y + r.height,
      r.x,           r.y + r.height
    ))

  override def draw(g: GdxGraphics): Unit = {
    if(!isvulnerable) g.setColor(Color.BLUE)else g.setColor(Color.PURPLE)
    g.drawFilledRectangle(x+width, y+height, width, height, 0)


  }
}
