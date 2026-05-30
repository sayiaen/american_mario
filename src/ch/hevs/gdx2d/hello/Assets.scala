package ch.hevs.gdx2d.hello
import com.badlogic.gdx.graphics.Texture
object Assets {

  var playerTex: Texture =_
  var player_2_Tex: Texture =_
  var minionTex: Texture =_
  var bossTex: Texture=_
  var boss_ph_2: Texture=_
  var boss_2_Tex: Texture=_
  var boss_2_ph_2_Tex: Texture=_
  var lootBoxTex: Texture = _
  var platformTex: Texture=_
  var RPGTex: Texture=_
  var backgroundTex: Texture=_

  def load(): Unit = {
    playerTex = new Texture("data/images/mario.png")
    player_2_Tex = new Texture("data/images/player_2.png")

    minionTex = new Texture("data/images/goomba.png")
    bossTex = new Texture("data/images/Boss.png")
    boss_ph_2 =new Texture("data/images/Boss_Phase2.png")
    boss_2_Tex = new Texture("data/images/boss_2.png")
    boss_2_ph_2_Tex = new Texture("data/images/boss_2_ph_2.png")
    lootBoxTex = new Texture("data/images/lootbox.png")
    platformTex = new Texture("data/images/brick.png")
    RPGTex  = new Texture("data/images/RPG.png")
    backgroundTex = new Texture("data/images/background_murica.png")

  }




}
