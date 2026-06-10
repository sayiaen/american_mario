package ch.hevs.gdx2d.hello
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

object Assets {
  //assets object is used for storing all the game textures, sound and background music

  var playerTex: Texture = _
  var player_2_Tex: Texture = _
  var minionTex: Texture = _
  var bossTex: Texture = _
  var boss_ph_2: Texture = _
  var boss_2_Tex: Texture = _
  var boss_2_ph_2_Tex: Texture = _
  var lootBoxTex: Texture = _
  var platformTex: Texture = _
  var RPGTex: Texture = _
  var backgroundTex: Texture = _
  var boss_hiddenTex: Texture = _
  var menubackgroundtex: Texture = _
  var gun_shootAu: Sound = _
  var level_upAu: Sound = _
  var lootBox_breakingau: Sound = _
  var player_diedau: Sound = _
  var rpgau: Sound = _
  var Won_the_gameau: Sound = _
  var hidden_soundau: Sound = _
  var game_sound: Music = _


  def load(): Unit = {
    playerTex = new Texture("data/images/mario.png")
    player_2_Tex = new Texture("data/images/player_2.png")
    boss_hiddenTex = new Texture("data/images/pngegg.png")

    minionTex = new Texture("data/images/goomba.png")
    bossTex = new Texture("data/images/Boss.png")
    boss_ph_2 = new Texture("data/images/Boss_Phase2.png")
    boss_2_Tex = new Texture("data/images/boss_2.png")
    boss_2_ph_2_Tex = new Texture("data/images/boss_2_ph_2.png")
    lootBoxTex = new Texture("data/images/lootbox.png")
    platformTex = new Texture("data/images/brick.png")
    RPGTex = new Texture("data/images/RPG.png")
    backgroundTex = new Texture("data/images/background_murica.png")
    menubackgroundtex =new Texture("data/images/mario_menu_background.jpg")
    game_sound = Gdx.audio.newMusic(Gdx.files.internal("data/sound_file/game_background_music.mp3"))
    hidden_soundau = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/XXXTENTACION - Moonlight (Judaism Version).mp3"))
    Won_the_gameau = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/won_the_game.mp3"))
    rpgau = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/RPG.mp3"))
    player_diedau = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/player_died.mp3"))
    lootBox_breakingau = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/Lootbox_breaking.mp3"))
    level_upAu = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/Level_up.mp3"))
    gun_shootAu = Gdx.audio.newSound(Gdx.files.internal("data/sound_file/gun_shoot.mp3"))


  }
}
