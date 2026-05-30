package ch.hevs.gdx2d.hello
import com.badlogic.gdx.graphics.Texture
object Assets {

  var playerTex: Texture =_
  var minionTex: Texture =_
  var bossTex: Texture=_
  var boss_ph_2: Texture=_
  var lootBoxTex: Texture = _
  var platformTex: Texture=_
  var RPGTex: Texture=_

  def load(): Unit = {
    playerTex = new Texture("data/images/mario.png")
    minionTex = new Texture("data/images/goomba.png")
    bossTex = new Texture("data/images/Boss.png")
    boss_ph_2 =new Texture("data/images/Boss_Phase2.png")
    lootBoxTex = new Texture("data/images/lootbox.png")
    platformTex = new Texture("data/images/brick.png")
    RPGTex  = new Texture("data/images/RPG.png")

  }




}
