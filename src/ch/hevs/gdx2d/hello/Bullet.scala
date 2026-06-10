package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.Platform
import com.badlogic.gdx.graphics.Color
//bullet class is used to calculate the spawning bullets, shooting and collision logics

class Bullet(var x: Float, var y: Float,val vx: Float, val vy: Float) {
  val width = 15f
  val height = 8f
  val damage = 10
//calcuulating the bullet physics thorugh velocity
  def update(dt: Float): Unit = {
    x += vx * dt
    y += vy * dt
  }
//drawing the bullet using the updated the x,y positons of the bullet
  def draw(g: GdxGraphics): Unit ={
    g.setColor(Color.GRAY)
    g.drawFilledRectangle(x + width/2, y+height/2, width, height, 0)
  }
//method to determine hit logic (bullet enem collisons)
  def hit(e:enemies): Boolean={ //got these calculations from gemini
    x < e.x + e.width &&
      x + width > e.x &&
      y < e.y + e.height &&
      y + height > e.y
  }
//bullet platform collisions)
  def hitPlatform(p: Platform): Boolean ={
    x < p.x + p.width &&
      x + width > p.x &&
      y < p.y + p.height &&
      y + height > p.y

  }

}
