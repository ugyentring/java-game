package my.games.earthguardian.gameobjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import my.games.earthguardian.commands.FireCommand;
import my.games.earthguardian.commands.GameInvoker;
import my.games.earthguardian.commands.MoveCommand;
import my.games.earthguardian.managers.GameManager;
import my.games.earthguardian.screen.GameScreen;


public class PlayerGameObject extends GameObject {
    private Touchpad touchpad;
    private Rectangle viewportBounds;
    private float step;
    private int lives;
    private boolean invincible;
    private float invincibleTime; // time left for invincibility
    private boolean isVisible; // for blinking effect
    private GameManager gameManager;
    private boolean isFiring;
    public GameScreen game;

    public PlayerGameObject(Vector2 position, Vector2 velocity, Texture texture, Touchpad touchpad, Rectangle viewportBounds) {
        super(position, velocity, texture, viewportBounds);
        this.bounds.setSize(1, 1);
        this.touchpad = touchpad;
        this.viewportBounds = viewportBounds;
        this.step = 0.1f;
        this.lives = 3;
        this.invincible = false;
        this.invincibleTime = 0;
        this.isVisible = true;
    }

    public void update(float deltaTime) {

        handleInput(deltaTime);

        if (invincible) {
            invincibleTime -= deltaTime;
            if (invincibleTime <= 0) {
                invincible = false;
                isVisible = true; // Ensure player is visible after invincibility ends
            } else {
                // Blink effect: alternate visibility every 0.2 seconds
                if ((int)(invincibleTime * 10) % 2 == 0) {
                    isVisible = true;
                } else {
                    isVisible = false;
                }
            }
        }

        super.update(deltaTime);
        clampPositionToViewport();
    }

    private void handleInput(float deltaTime) {
        GameInvoker invoker = new GameInvoker();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            invoker.setCommand(new MoveCommand(this, new Vector2(-step, 0)));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            invoker.setCommand(new MoveCommand(this, new Vector2(step, 0)));
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            invoker.setCommand(new MoveCommand(this, new Vector2(0, step)));
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            invoker.setCommand(new MoveCommand(this, new Vector2(0, -step)));
        } else {
            velocity.set(0, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            invoker.setCommand(new FireCommand(this));
            this.isFiring = true;
        } else {
            this.isFiring = false;
        }

        if (touchpad != null && Gdx.app.getType() == Application.ApplicationType.Android) {
            invoker.setCommand(new MoveCommand(this, new Vector2(touchpad.getKnobPercentX() * step,
                touchpad.getKnobPercentY() * step)));
        }
        invoker.invoke();

    }

    public void setGameManager(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public void move(Vector2 direction) {
        velocity.set(direction);
    }

    public void startFiring() {
        game.startFiring();
    }

    private void clampPositionToViewport() {
        // Ensure player stays within viewport bounds
        if (position.x < viewportBounds.x) {
            position.x = viewportBounds.x;
        } else if (position.x + this.bounds.width > viewportBounds.x + viewportBounds.width) {
            position.x = viewportBounds.x + viewportBounds.width - this.bounds.width;
        }

        if (position.y < viewportBounds.y) {
            position.y = viewportBounds.y;
        } else if (position.y + this.bounds.height > viewportBounds.y + viewportBounds.height) {
            position.y = viewportBounds.y + viewportBounds.height - this.bounds.height;
        }

        bounds.setPosition(position.x, position.y);
    }

    public void hitByEnemy() {
        setGameManager(this.gameManager);
        if (!invincible) {
            lives--;
            if (lives > 0) {
                activateInvincibility();
            } else {
                setRemove(true);
            }
        }
    }

    private void activateInvincibility() {
        invincible = true;
        invincibleTime = 2f;
        startBlinkingEffect();
    }

    private void startBlinkingEffect() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (!invincible) {
                    cancel();
                }
            }
        }, 0, 0.1f);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isVisible) {
            super.render(batch);
        }
    }

    public int getLives() {
        return lives;
    }

    public boolean isInvincible() {
        return invincible;
    }
}

