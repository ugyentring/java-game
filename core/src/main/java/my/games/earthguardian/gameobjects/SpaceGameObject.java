package my.games.earthguardian.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpaceGameObject extends GameObject {
    public SpaceGameObject(Vector2 position, Vector2 velocity, Texture texture, Rectangle viewportBounds){
        super(position, velocity, texture, viewportBounds);
    }

    public void update(float deltaTime){
        if(this.position.y <-16){
            this.position.y = 16;
            super.update(deltaTime);
        }
    }

    public boolean checkCollision(GameObject other){
        return false;
    }
}
