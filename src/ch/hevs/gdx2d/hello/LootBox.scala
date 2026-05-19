package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.{Entity, Platform}
import com.badlogic.gdx.graphics.Color


class LootBox (var startX: Float, var startY :Float) extends Entity{
  override var x: Float = startX
  override var y: Float = startY
  override var width: Float = 40f
  override var height: Float = 50f

  var health : Int = 5

  def takeDamage(amount: Int): Boolean ={
    health -= amount
    health <= 0
  }

  override def update(dt: Float, platform: List[Platform], camX: Float): Unit = {}//lootboxesdontmove so no need for update

    override def draw(g: GdxGraphics): Unit = {
     g.setColor(Color.GREEN)
     g.drawFilledRectangle(x+ width/2, y + height/2, width, height,0)
  }



}

