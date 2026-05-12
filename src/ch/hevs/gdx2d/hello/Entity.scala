package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.mygame.Platform
import ch.hevs.gdx2d.lib.GdxGraphics

trait Entity {
  var x: Float
  var y: Float
  var width: Float
  var height: Float

  def update(dt: Float, platform: List[Platform]): Unit
  def draw(g: GdxGraphics): Unit
}