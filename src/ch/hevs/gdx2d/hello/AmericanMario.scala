package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input
import ch.hevs.gdx2d.hello.{Boss, LootBox, Minion, enemies}

// THE RECURSIVE GENERATOR
object LevelBuilder {
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

  def buildEnemies(count: Int, validPlatforms: List[Platform], acc: List[enemies] = Nil): List[enemies] = {
    if (count <= 0 || validPlatforms.isEmpty) acc
    else {
      val p = validPlatforms(scala.util.Random.nextInt(validPlatforms.length))
      val e = new Minion(p.x + p.width / 2, p.y + p.height + 20f, p.width)
      buildEnemies(count - 1, validPlatforms, acc :+ e)
    }
  }

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
      val bossArenaW = 3000f
      val bossArena = new Platform(lastPlatform.x + lastPlatform.width + 150, lastPlatform.y, bossArenaW, 50f, isGoal = false)
      val GoalPlatform = new Platform(bossArena.x + bossArenaW + 50, bossArena.y, 200, 50, true)
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
}

// THE MAIN GAME CLASS
class AmericanMario extends PortableApplication(1920, 1080) {
  var player: Player = _
  var platforms = List[Platform]()

  var currentLevel = 1
  var maxLevels = AmericanMario.targetLevels

  override def onInit(): Unit = {
    setTitle(s"Mario - Level $currentLevel of $maxLevels")
    val (genPlatforms, genEnemies, genBoxes) = LevelBuilder.generateLevel(currentLevel, maxLevels)
    platforms = genPlatforms
    death_manager.init(genEnemies, genBoxes)
    player = new Player(platforms.head.x + 50f, platforms.head.y + 100f)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    val dt = Gdx.graphics.getDeltaTime

    g.clear(Color.WHITE)

    // Horizontal linear scrolling tracker variables
    val camX = if (player.x < 960) 0f else (player.x - 960)

    // Updates internal velocity variables, inputs, weapon spawns, and tile checks
    player.update(dt, platforms, camX)

    g.moveCamera(camX, 50f)
    platforms.foreach(p => p.draw(g))

    if (death_manager.checkStatus(player, dt, camX, platforms)) {
      println("DEATH - Restarting Level")
      onInit()
    }

    death_manager.draw(g, camX, player)
    player.draw(g)

    // Level progression check loop
    platforms.foreach(p =>
      if (p.isGoal && player.collidesWith(p)) {
        if (currentLevel < maxLevels) {
          println(s"Level $currentLevel Complete! Advancing...")
          currentLevel += 1
          onInit()
        } else {
          g.setColor(Color.RED)
          g.drawString(camX + 500, 600, "GAME BEAT! YOU WON!", 50)
          if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            currentLevel = 1
            onInit()
          }
        }
      }
    )
    g.drawFPS()
  }
}

object AmericanMario {
  var targetLevels: Int = 1

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