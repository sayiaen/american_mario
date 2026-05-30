package ch.hevs.gdx2d.hello
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Platform
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.{Intersector, Rectangle}

class Minion(val StartX: Float, val StartY: Float, val Range: Float) extends enemies(StartX, StartY, 40f, 40f, maxHealth = 2) {

  var speed = 120f
  var direction  =1
  var vx = speed
  var vy = 0f
  val gravity = -1000f





  override def update(dt: Float, platforms: List[Platform]): Unit = {
    // 1. Apply movement and gravity
    vx = speed * direction
    x += vx * dt
    var minionRect = new Rectangle(x, y, width, height)
    val mtv = new Intersector.MinimumTranslationVector()





    // 2. Collision with map
    for (p <- platforms) {
      val platformRect = new Rectangle(p.x, p.y, p.width, p.height)
      if (Intersector.overlapConvexPolygons(rectToPolygon(minionRect), rectToPolygon(platformRect), mtv)) {

        x += mtv.normal.x * mtv.depth


       if (math.abs(mtv.normal.x) > 0.5f) {
          // Hit a wall! Turn around.
          direction *= -1
        }
        minionRect.set(x, y, width, height)
      }
    }
    vy += gravity * dt
    y += vy * dt
    minionRect.set(x,y,width, height)
    var onGround = false

    for(p<- platforms){
      val platformrect = new Rectangle(p.x, p.y, p.width, p.height)
      if(Intersector.overlapConvexPolygons(rectToPolygon(minionRect), rectToPolygon(platformrect), mtv)){
        y += mtv.normal.y * mtv.depth
        if (mtv.normal.y > 0.5f) {
          vy = 0f
          onGround = true
        }
        minionRect.set(x, y, width, height)
      }
    }


    //Edge Detection
    if (onGround) {
      if (x > StartX + (Range / 2) - width) {
        direction = -1
      } else if (x < StartX - (Range / 2)) {
        direction = 1
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
    val region = new TextureRegion(Assets.minionTex)
    if(direction >0){
      region.flip(true, false)
    }
    g.draw(region, x, y, width, height)
    //g.setColor(Color.RED)
    //g.drawFilledRectangle(x + width/2, y + height/2, width, height,90)
  }
}


