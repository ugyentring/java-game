package my.games.earthguardian.gameobjects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import my.games.earthguardian.config.GameConfig;

public class BulletGameObject extends GameObject  {
    public BulletGameObject(Vector2 position, Vector2 velocity, Texture texture, Rectangle viewportBounds){
        super(position, velocity, texture, viewportBounds);
        this.bounds.setSize(0.1f,0.3f);
        this.viewportBounds = viewportBounds;
    }

    public void update(float deltaTime){
        super.update(deltaTime);
        // Remove the bullet if it goes off the top or bottom of the screen
        if (position.y > viewportBounds.height || position.y < 0) {
            this.setRemove(true);  // Remove bullet if off-screen
        }
    }
}
