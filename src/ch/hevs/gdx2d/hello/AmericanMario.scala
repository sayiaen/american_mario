package ch.hevs.gdx2d.mygame

import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input
import ch.hevs.gdx2d.hello.{Minion, enemies}

// THE RECURSIVE GENERATOR
object LevelBuilder {
  // generates platforms
  def buildPlatforms(count: Int, startX: Float, startY: Float, acc: List[Platform] = Nil): List[Platform] = {
    if (count <= 0) acc
    else {
      // Randomize platform sizes and gaps
      val width = scala.util.Random.between(250f, 450f)
      val gapX = scala.util.Random.between(150f, 250f)

      // Randomize Y, but keep it constrained so it's playable
      val nextY = math.max(100f, math.min(700f, startY + scala.util.Random.between(-200f, 200f)))

      val isGoal = count == 1 // If it's the last platform, make it the goal
      val p = new Platform(startX, startY, width, 50f, isGoal)

      buildPlatforms(count - 1, startX + width + gapX, nextY, acc :+ p)
    }
  }

  //generates enemies and places them on valid platforms
  def buildEnemies(count: Int, validPlatforms: List[Platform], acc: List[enemies] = Nil): List[enemies] = {
    if (count <= 0 || validPlatforms.isEmpty) acc
    else {
      val p = validPlatforms(scala.util.Random.nextInt(validPlatforms.length))
      // Spawn right in the middle of the platform, patrol range is exactly the platform's width!
      val e = new Minion(p.x + p.width / 2, p.y + p.height + 20f, p.width)
      buildEnemies(count - 1, validPlatforms, acc :+ e)
    }
  }

  def generateLevel(levelNum: Int): (List[Platform], List[enemies]) = {
    //scaling
    val pCount = 1 + ((levelNum + 1) * 4)
    val eCount = 4 + ((levelNum - 1) * 2)

    val platforms = buildPlatforms(pCount, 0f, 200f)

    //no enemies on the first and last platforms
    val validEnemyPlatforms = platforms.tail.filterNot(_.isGoal)
    val monsters = buildEnemies(eCount, validEnemyPlatforms)

    (platforms, monsters)
  }
}

// 2. THE MAIN GAME CLASS
class AmericanMario extends PortableApplication(1920, 1080) {
  var player: Player = _
  var platforms = List[Platform]()

  var currentLevel = 1
  var maxLevels = AmericanMario.targetLevels

  override def onInit(): Unit = {
    setTitle(s"Mario - Level $currentLevel of $maxLevels")

    // Call the recursive generator
    val (genPlatforms, genEnemies) = LevelBuilder.generateLevel(currentLevel)
    platforms = genPlatforms

    // Pass the generated enemies into the death manager
    death_manager.init(genEnemies)

    // Spawn player safely on the first platform
    player = new Player(platforms.head.x + 50f, platforms.head.y + 100f)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    val dt = Gdx.graphics.getDeltaTime

    player.vx = 0
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.vx = player.speed
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.vx = -player.speed

    if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.onGround) {
      player.vy = player.jumpForce
      player.onGround = false
    }

    g.clear(Color.WHITE)

    val camX = if(player.x < 960) 0f else (player.x - 960)
    player.update(dt, platforms, camX)

    g.moveCamera(camX, 50f)
    platforms.foreach(p => p.draw(g))

    if(death_manager.checkStatus(player, dt, camX, platforms)) {
      println("DEATH - Restarting Level")
      onInit()
    }

    death_manager.draw(g, camX, player)
    player.draw(g)

    // Level progression logic
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

    // Terminal input reading block
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