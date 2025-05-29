package my.games.earthguardian.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public interface AbstractGameObjectFactory {
    GameObject createScrollingScreen(Vector2 position, Vector2 velocity);
    GameObject createPlayer(Vector2 position, Vector2 velocity);
    GameObject createEnemy(Vector2 position, Vector2 velocity);
    GameObject createBullet(Vector2 position, Vector2 velocity);
}
