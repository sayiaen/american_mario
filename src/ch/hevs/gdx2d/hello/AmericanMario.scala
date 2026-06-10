package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.mygame.{Platform, Player, death_manager}
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle

// recursive generators
object LevelBuilder {
  //recursive function used for platform building
  def buildPlatforms(count: Int, startX: Float, startY: Float, acc: List[Platform] = Nil): List[Platform] = {
    if (count <= 0) acc
    else {
      val width = scala.util.Random.between(250f, 450f)
      val gapX = scala.util.Random.between(150f, 250f)
      val nextY = math.max(100f, math.min(700f, startY + scala.util.Random.between(-200f, 200f)))
      val p = new Platform(startX, startY, width, 50f, false)
      buildPlatforms(count - 1, startX + width + gapX, nextY, acc :+ p)
    }
  }
  //recursive function for enemy spawning
  def buildEnemies(count: Int, validPlatforms: List[Platform], acc: List[enemies] = Nil): List[enemies] = {
    if (count <= 0 || validPlatforms.isEmpty) acc
    else {
      val p = validPlatforms(scala.util.Random.nextInt(validPlatforms.length))
      val e = new Minion(p.x + p.width / 2, p.y + p.height + 20f, p.width)
      buildEnemies(count - 1, validPlatforms, acc :+ e)
    }
  }
  //recursive function for lootbox generation and spawning
  def buildLootBoxes(count: Int, validPlatforms: List[Platform], acc: List[LootBox] = Nil): List[LootBox] = {
    if (count <= 0 || validPlatforms.isEmpty) acc
    else {
      val p = validPlatforms(scala.util.Random.nextInt(validPlatforms.length))
      val spawnX = p.x + scala.util.Random.between(20f, math.max(21f, p.width - 60f))
      val spawnY = p.y + p.height + 60
      val lb = new LootBox(spawnX, spawnY)
      val restofPlatforms = validPlatforms.filterNot(_ == p)
      buildLootBoxes(count - 1, restofPlatforms, acc :+ lb)
    }
  }
  //method for generating levels using the previus 3 recursive functions
  //using the level count to determine enemy, platform and lootbox numbers
  //boss platform and final platform generation
  def generateLevel(levelNum: Int, maxLevel: Int): (List[Platform], List[enemies], List[LootBox]) = {
    val pCount = 1 + ((levelNum + 1) * 4)
    val eCount = 4 + ((levelNum - 1) * 2)
    val lbCount = 2 + (levelNum - 1)

    val platforms = buildPlatforms(pCount, 0f, 200f)
    val lastPlatform = platforms.last
    val validEnemyPlatforms = platforms.tail.filterNot(_.isGoal)
    val boxes = buildLootBoxes(lbCount, validEnemyPlatforms)
    val minions = buildEnemies(eCount, validEnemyPlatforms)

    if (levelNum == maxLevel) {
      val bossArenaW = 2000f
      val bossArena = new Platform(lastPlatform.x + lastPlatform.width + 150, lastPlatform.y, bossArenaW, 50f, isGoal = false)
      val GoalPlatform = new Platform(bossArena.x + bossArenaW + 100, bossArena.y + 100, 200, 50, true)
      val allPlatforms = platforms :+ bossArena :+ GoalPlatform

      val boss = new Boss(bossArena.x + bossArenaW / 2, bossArena.y + bossArena.height + 10, bossArena.x, bossArenaW)
      val monsters = minions :+ boss

      (allPlatforms, monsters, boxes)
    } else {
      val standardGoalPlatform = new Platform(lastPlatform.x + lastPlatform.width + 200f, lastPlatform.y, 300f, 50f, isGoal = true)
      val allPlatforms = platforms :+ standardGoalPlatform

      (allPlatforms, minions, boxes)
    }
  }


}

// THE MAIN GAME CLASS
class AmericanMario extends PortableApplication(1920, 1080) {
  var mouseclicked: Boolean = false // to evade multiple clicks
  var justFinished: Boolean = false
  var gameState: String = "MENU"
  var player: Player = _
  var platforms = List[Platform]()
  var maxcamX: Float  = 0

  var currentLevel = 1
  var maxLevels = AmericanMario.targetLevels


  //oninit method is the method to spawn all the preivously generated entities when game starts for the first time
  override def onInit(): Unit = {
    setTitle(s"Mario - Level $currentLevel of $maxLevels")
    // need to add a set function to put mario back in the beginning / gam eloop stuck because the variables stuck the same value and it keeps dying because health doesn't change
    Assets.load()
    if(Assets.game_sound != null){
      Assets.game_sound.setLooping(true)
      Assets.game_sound.setVolume(0.2f)
      Assets.game_sound.play()
    }

  }

  // iitlevel method is used to generate and update the entities when player is dead or level passed
  def initLevel(): Unit = {
    val (genPlatforms, genEnemies, genBoxes) = LevelBuilder.generateLevel(currentLevel, maxLevels)
    platforms = genPlatforms
    death_manager.init(genEnemies, genBoxes)
    player = new Player(platforms.head.x + 50f, platforms.head.y + 100f)
    maxcamX = 0

  }

  //ongraphic is used to generate the menu graphics, updating the player/enemy/platform entities
  override def onGraphicRender(g: GdxGraphics): Unit = {
    val dt = Gdx.graphics.getDeltaTime
    gameState match {
      case "MENU" =>
        g.moveCamera(0, 0)
        //g.clear(Color.DARK_GRAY)
        g.draw(Assets.menubackgroundtex, 0, 0,1920, 1080)

        g.setColor(Color.WHITE)
        g.drawString(450, 950, "AMERICAN MARIO", 40)
        g.drawString(450, 850, s"CURRENT SCORE = ${GameManager.totalScore}", 24)

        val startbtn = new Rectangle(800f, 500f, 300f, 80f)
        val secondbossbtn = new Rectangle(1700f,900f,200f, 50f)
        val secondPlayerbtn = new Rectangle(1700f, 1000f, 200f, 50f)

        //val StartbtnX = 800f;
        //val StartbtnY = 500f;
        //val StartbtnW = 300f;
        //val StartbtnH = 80f
        //val SecondbossbtnX = 1700f
        //val SecondbossbtnY = 900f
        //val SecondbossbtnW = 200f
        //val SecondbossbtnH = 50f
        //val SecondPlayerbtnX = 1700f
        //val SecondPlayerbtnY = 1000f
        //val SecondPlayerbtnW  = 200f
        //val SecondPlayerbtnH  = 50f



        // rectangle for button de start
        g.setColor(Color.GREEN)
        g.drawFilledRectangle(startbtn.x + startbtn.width / 2, startbtn.y + startbtn.height / 2, startbtn.width, startbtn.height, 0)
        g.setColor(Color.BLACK)
        g.drawString(startbtn.x + 60, startbtn.y + 50, "START GAME", 24)

        // rectangle for button boss change
        g.setColor(Color.BLUE)
        g.drawFilledRectangle(secondbossbtn.x + secondbossbtn.width/2, secondbossbtn.y + secondbossbtn.height/2, secondbossbtn.width, secondbossbtn.height, 0)
        g.setColor(Color.BLACK)
        g.drawString(secondbossbtn.x + 10, secondbossbtn.y + 10, "2000 PTs for Second boss", 24)

        // rectangle for button player change
        g.setColor(Color.YELLOW)
        g.drawFilledRectangle(secondPlayerbtn.x + secondPlayerbtn.width/2, secondPlayerbtn.y + secondPlayerbtn.height/2, secondPlayerbtn.width, secondPlayerbtn.height, 0)
        g.setColor(Color.BLACK)
        g.drawString(secondPlayerbtn.x + 10, secondPlayerbtn.y + 10, "1000 PTs for 2. Player skin", 24)


        // MOUSE HIT DETECTION LOGIC: taken by ai to not waste too much time

        val mouseX = Gdx.input.getX.toFloat
        val mouseY = 1080f - Gdx.input.getY.toFloat

        val ismousecurrentlypressed = Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)


        if (ismousecurrentlypressed && !mouseclicked) {
          if (startbtn.contains(mouseX, mouseY)) {
            gameState = "PLAY"
            initLevel()
          }
          if (secondbossbtn.contains(mouseX, mouseY)) {
            if (GameManager.totalScore >= 2000) {
              GameManager.totalScore = GameManager.totalScore - 2000
              GameManager.selectbossskin = 1
              println("boss skin changed")
            } else {
              println("Not enough points, KILL MORE")

            }
          }
            if (secondPlayerbtn.contains(mouseX, mouseY)) {
              if (GameManager.totalScore >= 1000) {
                GameManager.totalScore = GameManager.totalScore - 1000
                GameManager.selectmarioskin = 1
                println("player skin changed")
              }
              else {
                println("Not enough points, KILL MORE")

              }
            }

          }

      case "PLAY" =>


        g.clear(Color.WHITE)


        // Horizontal tracker available, vertical doesn't work(didn't use much but still want to know how to)
        val camX = if (player.x < 960) 0f else (player.x - 960)
        maxcamX = math.max(maxcamX, camX)
        val camY =if (player.y <= 540) 0f else (player.y - 540)
        //println(s"${player.y}")
        //println(s"${camY}")


        // Updates internal velocity variables, inputs, weapon spawns, and tile checks
        player.update(dt, platforms, maxcamX)
        if(player.x < maxcamX) player.x = maxcamX

        g.moveCamera(maxcamX, camY)
        g.draw(Assets.backgroundTex, maxcamX, 0, 1920, 1080) //used to crash after the first death, it was a onit() and initlevel() issue

        platforms.foreach(p => p.draw(g))

        if (death_manager.checkStatus(player, dt, maxcamX, platforms)) {
          println("DEATH - Restarting Level")
          Assets.player_diedau.play()
          initLevel()//onInit()// CHARGER LE DEBUT DE JEUX
        }

        death_manager.draw(g, maxcamX, player)
        player.draw(g)

        // Level progression check loop
        platforms.foreach(p =>
          if (p.isGoal && player.collidesWith(p)) {
            if (currentLevel < maxLevels) {
              println(s"Level $currentLevel Complete! Advancing...")
              currentLevel += 1
              Assets.level_upAu.play()
              initLevel()//onInit()
            } else {
              g.setColor(Color.RED)
              g.drawString(maxcamX + 500, 600, "GAME BEAT! YOU WON!", 50)
              if(!justFinished) {
                justFinished = true
                Assets.Won_the_gameau.play() // take down the repetitive game won signal !!!!!! //antoine m'a aidé
              }


              if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                gameState = "MENU"
                //currentLevel = 1
                //initLevel()//onInit()
              }
            }
          }
        )
        g.drawFPS()
    }
    }
  }

//object containing the main game running method and number of levels choice through terminal
  object AmericanMario {
    var targetLevels: Int = _

    def main(args: Array[String]): Unit = {
      println("======================================")
      println("      WELCOME TO MARIO GENERATOR      ")
      println("======================================")
      print("Enter the number of levels to generate: ")

      try {
        targetLevels = scala.io.StdIn.readInt()
        if (targetLevels <= 0) targetLevels = 1
      } catch {
        case _: Throwable =>
          println("Invalid input, defaulting to 3 levels.")
          targetLevels = 3
      }
          new AmericanMario()



    }
}