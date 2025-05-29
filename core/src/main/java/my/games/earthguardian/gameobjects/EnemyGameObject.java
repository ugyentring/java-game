package my.games.earthguardian.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import my.games.earthguardian.managers.GameManager;
import my.games.earthguardian.managers.ResourceManager;

public class EnemyGameObject extends GameObject {
    private float timeSinceLastShot;
    private float shootingInterval;
    private GameManager gameManager;

    public EnemyGameObject(Vector2 position, Vector2 velocity, Texture texture, Rectangle viewportBounds){
        super(position, velocity, texture, viewportBounds);
        this.bounds.setSize(0.8f,1);
        this.viewportBounds = viewportBounds;
        this.shootingInterval = 3.0f +(float) Math.random()*2.0f;
        this.timeSinceLastShot = 0.0f;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void update(float deltaTime){
        super.update(deltaTime);  // This now handles velocity scaled by deltaTime
        // If enemy moves below the screen, reset it to the top

        timeSinceLastShot += deltaTime;

        if(timeSinceLastShot >= shootingInterval) {
            shootLaser();
            timeSinceLastShot = 0.0f;
        }

//        if (position.y < 0) {
//            position.y = viewportBounds.height;  // Reset enemy to top of the screen
//            position.x = (float) Math.random() * (viewportBounds.width - bounds.width);  // Random x position
//        }

    }

    private void shootLaser(){
        Vector2 laserPosition = new Vector2(position.x + bounds.width /2, position.y);
        Vector2 laserSpeed = new Vector2(0, -0.1f);

        Texture laser = ResourceManager.getInstance().getAsset("images/blue_laser.png", Texture.class);
        EnemyBulletGameObject alienLaser = new EnemyBulletGameObject(laserPosition, laserSpeed, laser,viewportBounds);
        gameManager.addGameObject(alienLaser);
    }

}
