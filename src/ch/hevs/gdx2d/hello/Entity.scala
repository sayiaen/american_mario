package ch.hevs.gdx2d.mygame
import com.badlogic.gdx.math.Rectangle

import ch.hevs.gdx2d.lib.GdxGraphics

//used as a motherclass for all entitites in the game, let it be player, platforms, enemies, bullets and grenades

trait Entity {
  var x: Float
  var y: Float
  var width: Float
  var height: Float


  def update(dt: Float, platform: List[Platform], camX: Float): Unit
  def draw(g: GdxGraphics): Unit
  def getbounds: Rectangle = new Rectangle(x, y, width, height) // centralised methods since all entities collide at some point
}