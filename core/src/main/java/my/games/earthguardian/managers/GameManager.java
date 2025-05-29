package my.games.earthguardian.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import my.games.earthguardian.gameobjects.BulletGameObject;
import my.games.earthguardian.gameobjects.EnemyBulletGameObject;
import my.games.earthguardian.gameobjects.EnemyGameObject;
import my.games.earthguardian.gameobjects.GameObject;
import my.games.earthguardian.gameobjects.PlayerGameObject;

public class GameManager {
    private static GameManager instance;
    private Array<GameObject> gameObjects;
    // Private constructor to prevent instantiation
    private GameManager() {
        gameObjects = new Array<>();
    }
    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    // Add a game object to the manager
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }
    // Remove a game object from the manager
    public void removeGameObject(GameObject gameObject) {
        gameObjects.removeValue(gameObject, true);
    }
    // Update all game objects
    public void update(float deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
        checkCollisions();
        removeMarkedObjects();
    }
    // Render all game objects
    public void render(SpriteBatch batch) {
        for (GameObject gameObject : gameObjects) {
            gameObject.render(batch);
        }
    }
    // Dispose all game objects
    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            gameObject.dispose();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
    private boolean gameOver;
    public int getScore() {
        return score;
    }
    private int score;

    // Check for collisions between game objects
    private void checkCollisions() {
        for (int i = 0; i < gameObjects.size; i++) {
            for (int j = i + 1; j < gameObjects.size; j++) {
                GameObject obj1 = gameObjects.get(i);
                GameObject obj2 = gameObjects.get(j);

                // Check if these two objects collide
                if (obj1.checkCollision(obj2)) {

                    // Case 1: Bullet hits an enemy (remove both)
                    if (obj1 instanceof BulletGameObject && obj2 instanceof EnemyGameObject) {
                        System.out.println("Bullet hit enemy type 1");
                        obj1.setRemove(true); // Remove the bullet
                        obj2.setRemove(true); // Remove the enemy
                        score+=4000;
                    } else if (obj1 instanceof EnemyGameObject && obj2 instanceof BulletGameObject) {
                        System.out.println("Bullet hit enemy type 2");
                        obj1.setRemove(true); // Remove the enemy
                        obj2.setRemove(true); // Remove the bullet
                        score+=4000;
                    }

                    // Case 2: Bullet hits the player (reduce life)
                    if (obj1 instanceof EnemyBulletGameObject && obj2 instanceof PlayerGameObject) {
                        PlayerGameObject player = (PlayerGameObject) obj2;
                        if (!player.isInvincible()) {
                            System.out.println("Player hit by bullet type 3");
                            player.hitByEnemy(); // Reduce life, activate invincibility
                        }
                        if(player.getLives() <=0){
                            gameOver = true;
                        }
                        obj1.setRemove(true); // Remove the bullet
                    } else if (obj1 instanceof PlayerGameObject && obj2 instanceof EnemyBulletGameObject) {
                        PlayerGameObject player = (PlayerGameObject) obj1;
                        if (!player.isInvincible()) {
                            System.out.println("Player hit by bullet type 4");
                            player.hitByEnemy(); // Reduce life, activate invincibility
                        }
                        if(player.getLives() <=0){
                            gameOver = true;
                        }
                        obj2.setRemove(true); // Remove the bullet
                    }

                    // Case 3: Enemy Hits Player (reduce life)
                    if (obj1 instanceof EnemyGameObject && obj2 instanceof PlayerGameObject) {
                        PlayerGameObject player = (PlayerGameObject) obj2;
                        if (!player.isInvincible()) {
                            System.out.println("Player hit by bullet type 5");
                            player.hitByEnemy(); // Reduce life, activate invincibility
                        }
                        if (player.getLives() <= 0) {
                            gameOver = true;
                        }
                        obj1.setRemove(true); // Remove the bullet
                    } else if (obj1 instanceof PlayerGameObject && obj2 instanceof EnemyGameObject) {
                        PlayerGameObject player = (PlayerGameObject) obj1;
                        if (!player.isInvincible()) {
                            System.out.println("Player hit by bullet type 6");
                            player.hitByEnemy(); // Reduce life, activate invincibility
                        }
                        if(player.getLives() <=0){
                            gameOver = true;
                        }
                        obj2.setRemove(true); // Remove the bullet
                    }
                }
            }
        }
    }

    private void checkAlienLeft() {
        int count = 0;
        for (int i = 0; i < gameObjects.size; i++)
            if(gameObjects.get(i).getClass() == EnemyGameObject.class ) count++;
        if(count <=0) gameOver = true;
    }
    public void resetGame(){
        score = 0;
        gameOver = false;
        gameObjects.clear();
    }

    public void removeMarkedObjects() {
        for (int i = gameObjects.size - 1; i >= 0; i--) {
            if (gameObjects.get(i).isRemove()) {
                gameObjects.removeIndex(i);
            }
        }
    }
}
