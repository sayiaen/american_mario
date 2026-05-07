package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color


class AmericanMario extends PortableApplication(1920, 1080) {
  var player: Player =_

  override def onInit(): Unit = {
    setTitle("mario-phase1")
    player = new Player(200, 100)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear(Color.WHITE)
    val dt = Gdx.graphics.getDeltaTime
    player.update(dt)
    player.draw(g)

  }
}
object AmericanMario {
  def main(args: Array[String]): Unit = {
    new AmericanMario()
  }
}




