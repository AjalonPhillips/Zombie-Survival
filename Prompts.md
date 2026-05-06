Prompt 1: 
I'm building a top-down roguelike zombie survival game in Java using Swing, split into three files: GameModel.java, GameView.java, and GameController.java.

GameView should extend JPanel and be hosted in a JFrame.

GameController should contain the main method and connect the model and view.

GameModel must contain all game logic and have no Swing imports.

For now, just create the three class shells with placeholder comments describing what each class will do.

The program should compile and open a blank window.

Prompt 2 
Also include basic placeholders for:
- Player
- Enemy (zombie)
- Bullet

Do not implement gameplay yet, just structure and comments.

Prompt 3 
Refactor the project so that Player, Zombie, and Bullet are separate Java files.

Requirements:
- Create Player.java, Zombie.java, and Bullet.java
- Remove inner classes from GameModel
- Each class should be in its own file
- Update GameModel to use these classes
- Keep MVC structure intact
- Do not change unrelated code

Prompt 4 
Add player movement.

Requirements:
- Player should move using WASD keys
- Movement should be smooth (update position every frame)
- Store player position in GameModel
- Handle input in GameController
- Render player in GameView as a simple rectangle

Do NOT add enemies or shooting yet.
Do NOT refactor unrelated code.

Prompt 5 
Add zombies to the game.

Requirements:
- Create a list of Zombie objects in GameModel
- Spawn a zombie every 2 seconds at random screen edges
- Zombies should move toward the player position
- Render zombies in GameView as simple shapes

Do NOT add combat yet.
Keep existing movement working.

Prompt 6 
Add shooting mechanics.

Requirements:
- Player can shoot using spacebar
- Create Bullet objects with direction
- Store bullets in GameModel
- Update bullet positions each frame
- Remove bullets when off screen
- Render bullets in GameView

Do NOT modify zombie behavior yet.

Prompt 7
Add a survival system.

Requirements:
- Add a timer showing how long the player has survived
- Increase zombie spawn rate over time
- Gradually increase zombie speed
- Display survival time in GameView

Do NOT change existing gameplay mechanics.

Prompt 8 
Add a power-up system.

Requirements:
- Every 30 seconds, give the player a random upgrade
- Upgrades include:
  - Increase movement speed
  - Increase bullet speed
  - Increase fire rate
  - Restore health

Create a simple UpgradeManager class

Do NOT add UI selection yet — apply upgrades automatically.

Prompt 9 
Improve the power-up system.

Requirements:
- Pause the game every 30 seconds
- Show 3 upgrade choices on screen
- Let the player select using keys (1, 2, 3)
- Apply the selected upgrade
- Resume the game

Keep UI simple using Swing drawing.

Prompt 10
Add polish to the game.

Requirements:
- Add a start screen
- Add a game over screen
- Allow restarting the game
- Display score and health clearly
- Keep code organized and readable

Do NOT rewrite the entire project.

Prompt 11
For further polish, 
can you make it so when the window opens the first window that pops up is a choice bewtween having the game windowed and fullscreen? If the user should choose fullscreen, can the boarders in the game (Spawn Points & playable area) be changed to the fullscreen resolution?

Prompt 12
After the screen asking what perferred resolution, before the game instantly starts can you add a screen before hand that has the options "Start game", "Objective", & "Quit"

Prompt 13
Can you add one more option to the opening screen that is titles "Options"? in this menu it gives the ability to choose core gameplay controls. Changing the shoot button between spacebar and left mouse click.

Prompt 14
Troubleshooting
In the options menu the shooting preference is toggled with T and Enter and leave with the esc key, can you make it so enter is the key that returns to the menu?

Prompt 15
Now inside the game, when playing add a pause button right under the survival counter? in the pause menu add button that  offer resume and quit attempt. If the user should choose quit attempt bring them back to the start menu

Prompt 16 
Troubleshooting
The pause button works only on click. Thats fine I like the option between using P and clicking it to pause. 2 problems, P doesnt work to pause the game and when clicking the button the screen goes grey and theres no interface and theres no way to exit

Prompt 17
Can you make it so the screen boarders respective to windowed or full screen are the limits to player movemnet? Right now the player can travel outside of th boarders thus outside the playable area 

Prompt 18
Can a quality of life change be added where if the player chooses for their shoot button to be a mouse click that instead of firing the way their traveling they shoot where their cursor is?

Prompt 19
Add:
Mouse Crosshair
Damage indicators (flash red)
Health bar over player icon
High Score tracker
zombie variety (sprinters light blue, brutes dark green)
Temporary powerup drops
Reload mechanics
Paricle effects 

Prompt 20 
Can all powerups gained flash what was gained at the center of the screen? the text should fade away after about 3 seconds

Prompt 21 
Can some enviromnetal effects be added to make gameplay be a bit better?

Prompt 22
In blooddecal.java can it be so the blood isnt permanant? Its almost like the circles left behind are power ups or enemies and overtime could confuse the player

Prompt 23 
For simplicity remove the option for firing mode i the settign and just have both spacebar and a mouse click be the firing button. Because the setting should be empty give the option to have the background be detailed (rain, darkness, and lighting) be toggleable 