package ch.hevs.gdx2d.mygame
import ch.hevs.gdx2d.hello.{Minion, enemies}
import com.badlogic.gdx.graphics.Color
import ch.hevs.gdx2d.lib.GdxGraphics

import scala.collection.mutable.ListBuffer

object death_manager {
  val Lava_level  = 50f
  val enemies = ListBuffer[enemies]()

  def init(): Unit = {
    enemies.clear()
    enemies += new Minion(900, 300, 200)
    enemies += new Minion(2200, 400, 300)
  }

  def died(player: Player, dt: Float) : Boolean ={
    //lava
    if(player.y < Lava_level) return true

    var Monster_Collision = false
    for(e <- enemies) {
      e.update(dt)
      if(e.KillChck(player)) Monster_Collision =true
    }

    Monster_Collision

  }

  def draw(g: GdxGraphics, camX: Float): Unit = {
    g.setColor(Color.ORANGE)
    g.drawFilledRectangle(camX +800, Lava_level/2, 1920, Lava_level, 0)
     //red/orange lava /color is not showing/fixed it was an order issue

    g.setColor(Color.WHITE)
    g.drawString(camX+800, 75, "Danger: GET BURNED", 40)

    enemies.foreach((_.draw(g)))
  }
}
