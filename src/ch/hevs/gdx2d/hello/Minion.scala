package ch.hevs.gdx2d.hello
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class Minion(StartX: Float, StartY: Float, val Range:Float) extends enemies(StartX, StartY, 40f, 40f, maxHealth = 2){

  var speed = 120f
  var direction  =1
  val initialX = StartX

  override def update(dt: Float): Unit = {
    x += speed * direction * dt
    if (x > initialX + Range) direction = -1
    if (x < initialX) direction = 1
  }

  override def draw(g: GdxGraphics): Unit = {
    g.setColor(Color.RED)
    g.drawFilledRectangle(x + width/2, y + height/2, width, height,90)
  }
}


