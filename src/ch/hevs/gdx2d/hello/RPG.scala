package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Platform
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Intersector, Rectangle}

class RPG (var x: Float, var y: Float, var vx: Float, var vy: Float) {
  val width = 16
  val height = 16
  val gravity = -1000

  var explode = false

def update(dt: Float, platforms: List[Platform]): Unit ={


  vy += gravity*dt
  x += vx *dt
  y += vy * dt

  val grenadeRect  =new Rectangle(x, y, width, height)//to make the collision logic more proper


  for(p <- platforms if !explode){
    val platformRect = new Rectangle(p.x, p.y, p.width, p.height)
    if (Intersector.overlapConvexPolygons(rectToPolygon(grenadeRect), rectToPolygon(platformRect), null)) {
      // Impact detected! Mark as dead immediately to detonate
      explode = true
    }
  }
}
  def draw(g:GdxGraphics):Unit = {//flashing effcet to visualise the timer
   g.draw(Assets.RPGTex, x, y, width, height)


  }
  private def rectToPolygon(r: Rectangle): com.badlogic.gdx.math.Polygon = {
    val vertices = Array(
      r.x, r.y,
      r.x + r.width, r.y,
      r.x + r.width, r.y + r.height,
      r.x, r.y + r.height
    )
    new com.badlogic.gdx.math.Polygon(vertices)
  }
}
