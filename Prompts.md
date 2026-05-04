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