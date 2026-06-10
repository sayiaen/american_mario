# Welcome To American-Mario
* Where your classical-beloved Mario turns American "Pew-Pew"
# Procedural 2D Platformer
* A procedurally generated 2D platformer game built in Scala using the gdx2d framework (a custom wrapper around LibGDX).
* The game features dynamic difficulty scaling, a pure tail-recursive world generator, custom physics/collision handling, and a classic retro scrolling camera that locks to prevent backward backtracking.
# Features
* Level Generation: Levels are not hardcoded. They are built algorithmically using immutable data structures and recursion.
* Dynamic Difficulty System: The number of platforms, enemies, and items automatically scale up as the player advances through levels.
* Custom Character Cosmetics Shop: Main menu features an interactive bounding-box button market using LibGDX Rectangle components. Players can spend their accumulated scores on custom graphical upgrades like Phase-Shifted Boss Styles ($2000 \text{ PTs}$) or alternative Hero Player Skins ($1000 \text{ PTs}$).
* Boss Arena: The final level alters the procedural engine to build a massive final boss arena featuring a custom unique entity (Boss) that expands in scale dynamically through multiple combat phases based on incoming hit detection.
* Retro Screen-Locking Camera: Implements a modern horizontal camera latching system (maxcamX). Mario can look ahead, but the camera refuses to track backward, locking the player inside the visible screen just like the original NES Super Mario Bros.
* Vertical Camera Tracking: The camera remains grounded when Mario is low, but smoothly starts tracking vertically once the player jumps past the middle-point threshold of the screen ($540\text{px}$).
# Architecture & Code Breakdown
The core engine is split into two specialized components:
1. LevelBuilder (The Generation Engine)An isolated immutable object utilizing tail-recursive design patterns to safe-guard against stack overflow issues while spawning game entities.
* BuildPlatforms: Generates safe paths of jumping platforms with randomized widths, horizontal gaps, and vertical variations clamped between a safe zone ($100\text{px}$ to $700\text{px}$). 
* BuildEnemies: Randomly distributes walking Minion enemies on valid structural elements.
* BuildLootBoxes: Spawns destructible/interactable items along the paths while filtering out spent platforms to prevent overlapping coordinates.
* GenerateLevel: Combines all assets, computes difficulty scaling parameters according to currentLevel, and appends the final Goal or Boss Arena configuration.
2. AmericanMario (The Game Lifecycle & Loop)Extends PortableApplication from gdx2d, processing the full game rendering lifecycle:
* onInit(): Runs once at startup to trigger global VRAM texture caching via Assets.load().
* initLevel(): Disposes of the previous layout, re-generates a fresh level matrix, clears camera memory states, and respawns the Player back at the structural origin safely.
* onGraphicRender(): The main pipeline execution logic split into three primary structural pipelines via Scala Pattern Matching:
* MENU: Standard mouse-hit bounding-box checking interface utilizing a manual boolean state-latch debounce tracker (mouseclicked) to prevent runaway frame execution. This ensures rapid-fire OpenGL rendering loops only register a single skin purchase or state-transition event per clean, physical mouse click.
* PLAY: Runs real-time frame update ticks (player.update), checks screen-boundary edge clipping, processes asset rendering with proper texture mapping dependencies that handle orientation flipping, and runs continuous flag validation for player lifecycle (death_manager) or progression.
# Game Controls & UI
1. CLI Initialization: Upon startup, input the target number of levels you wish to clear directly into your system terminal.
2. Menu Navigation: Click the green START GAME box, blue BOSS SKIN box, or yellow PLAYER SKIN box with your Left Mouse Button.
3. In-Game Movement: (Controlled via standard configuration inside your Player class).
* LEFT / RIGHT ARROWS: Move horizontally.
* UP ARROW: Jump (Only when on ground).
* F Key: Fire standard horizontal bullets.
* G Key: Fire heavy, arcing RPG ballistic missiles (Consumes ammo acquired from LootBoxes).
* Screen Borders: You cannot backtrack off the left edge of the screen once the camera has progressed forward.
* Victory Restart: Once the game is beaten, press SPACE on the victory banner to reset back to the main menu.
# Technical Stack & Prerequisites
* Language: Scala (Utilizing functional design principles & immutable List collection pipelines).
* Engine Core: LibGDX (OpenGL-based cross-platform framework).
* Build Target: Developed under the ch.hevs.gdx2d workspace wrapper architecture (HES-SO Valais/Wallis custom framework).
* Window Dimensions: Native $1920 \times 1080$ Full HD resolution context window.
# Maintenance Notes & Fixed Anomalies
* During the engineering lifecycle, several critical platforming bugs were addressed and documented:
1. Menu-Click Action Multi-Firing: Fixed a bug where a single click on a skin purchase button would repeat 6–8 times, draining the player's score instantly. Resolved by implementing a custom input debounce latch (mouseclicked) to block continuous frame processing until the mouse button is physically released.
2. Unreachable Player Skin Match Branches: Corrected a pattern-matching flaw where fallback wildcard patterns (case _) were positioned above explicit filters. Reordered match architectures to process custom indices first before tumbling down to structural defaults.
3. Player Texture Render Hardcoding: Fixed a bug where unlocked character skins would not display because the TextureRegion rendering initialization logic was hardcoded to default sprite paths instead of using evaluated conditional variables.
4. Infinite Death State Loop: Replaced recursive onInit lifecycle calls with specialized initLevel pipeline resets to ensure player coordinates and target parameters are wiped and instantiated correctly on character mortality checks.
5. UI Alignment Drift: Bound HUD components (death_manager.draw) directly onto the permanent maxcamX horizontal anchor vector so user health trackers and weapons UI remain statically pinned over the player field of view.
6. Asset Context Disposals: Addressed background rendering crashes under frame rendering by restricting volatile resource purges inside external global managers.
7. Recursive function problems: to find the necessarey level generation functions, I spent hours and hours on research and trial/error rounds
# Game Preview
![Capture d'écran 2026-06-09 223932.png](data/images/Capture%20d%27%C3%A9cran%202026-06-09%20223932.png)
![Capture d'écran 2026-06-09 223952.png](data/images/Capture%20d%27%C3%A9cran%202026-06-09%20223952.png)
![Capture d'écran 2026-06-06 213045.png](data/images/Capture%20d%27%C3%A9cran%202026-06-06%20213045.png)
![Capture d'écran 2026-06-06 213106.png](data/images/Capture%20d%27%C3%A9cran%202026-06-06%20213106.png)
To see the gameplay you can download the preview video from the following link:
[game_preview_2.mp4](data/game_preview/game_preview_2.mp4)

