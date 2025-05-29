package my.games.earthguardian.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import my.games.earthguardian.managers.ResourceManager;


public class EGGameObjectFactory implements AbstractGameObjectFactory {
    private Touchpad touchpad;
    private Rectangle viewportBounds;

    public EGGameObjectFactory(Rectangle viewportBounds, Touchpad touchpad){
        this.viewportBounds = viewportBounds;
        this.touchpad = touchpad;
    }

    public GameObject createScrollingScreen(Vector2 position, Vector2 velocity){
        Texture texture = ResourceManager.getInstance().getAsset(
            "images/space_bgd.png", Texture.class
        );
        return new SpaceGameObject(position, velocity, texture,viewportBounds);
    }

    public GameObject createPlayer(Vector2 position, Vector2 velocity){
        Texture texture = ResourceManager.getInstance().getAsset(
            "images/fighter.png",Texture.class
        );
        return new PlayerGameObject(position, velocity, texture,touchpad,viewportBounds);
    }

    public GameObject createEnemy(Vector2 position, Vector2 velocity){
        Texture texture = ResourceManager.getInstance().getAsset(
            "images/alien1.png", Texture.class
        );
        return new EnemyGameObject(position, velocity, texture,viewportBounds);
    }

    public GameObject createBullet(Vector2 position, Vector2 velocity){
        Texture texture = ResourceManager.getInstance().getAsset(
            "images/laser.png", Texture.class
        );
        return new BulletGameObject(position, velocity, texture,viewportBounds);
    }
}
