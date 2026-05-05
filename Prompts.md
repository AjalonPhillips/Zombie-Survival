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