package my.games.earthguardian.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import my.games.earthguardian.EarthGuardianGame;
import my.games.earthguardian.commands.ExitCommand;
import my.games.earthguardian.commands.FireCommand;
import my.games.earthguardian.commands.GameInvoker;
import my.games.earthguardian.config.GameConfig;
import my.games.earthguardian.gameobjects.AbstractGameObjectFactory;
import my.games.earthguardian.gameobjects.EGGameObjectFactory;
import my.games.earthguardian.gameobjects.GameObject;
import my.games.earthguardian.gameobjects.PlayerGameObject;
import my.games.earthguardian.managers.GameManager;
import my.games.earthguardian.managers.ResourceManager;
import my.games.earthguardian.services.GlobalHighScoreServiceProxy;
import my.games.earthguardian.services.HighScoreService;
import my.games.earthguardian.services.LocalHighScoreServiceProxy;
import my.games.earthguardian.utils.UIFactory;
import my.games.earthguardian.utils.ViewportUtils;
import com.badlogic.gdx.Input;


public class GameScreen extends ScreenTemplate {// Reference to the main game class
    private GameManager gameManager;
    // UI elements
    private TextButton playExit;
    private TextButton fireButton;
    private TextButton lives;
    private Touchpad touchpad;
    private Skin skin;
    private Stage stage;
    // Music for the game
    private Music bgmGame;
    // Rendering components
    private ShapeRenderer shapeRenderer; // For drawing shapes (used for debugging)
    private SpriteBatch batch; // For drawing textures
// Cameras and viewports
private OrthographicCamera camera; // For 2D rendering
    private Viewport viewport; // To handle screen dimensions and scaling
    private OrthographicCamera hudCamera; // For HUD (Heads-Up Display) rendering
    private Viewport hudViewport; // For HUD rendering
    // Fonts
    private BitmapFont font; // For rendering text
    // Game state flags
    private boolean loadingComplete; // Flag to indicate if loading is complete
    private boolean debug; // Flag to enable/disable debug mode
    public boolean isFiring; // Flag to indicate if the player is firing
    public int enemyCount;
    public PlayerGameObject player;
    public EGGameObjectFactory gameObjectFactory;
    private HighScoreService localHighScoreService;
    private HighScoreService globalHighScoreService;
    private Dialog dialog;
    private GameInvoker invoker;


    // Game objects and managers
// Constructor to initialize the GameScreen with the main game reference

    public GameScreen(EarthGuardianGame game) {
        super(game);
        invoker = new GameInvoker();
        // Retrieve and play the background music if not already initialized
        bgmGame = ResourceManager.getInstance().getAsset("sounds/music/game_bgm.mp3", Music.class);
        bgmGame.setLooping(true);
        bgmGame.play();
// Get the SpriteBatch from the game
        this.batch = game.getBatch();
// Load the skin (UI elements)
        skin = ResourceManager.getInstance().getAsset("skins/neon-ui.json", Skin.class);
//Number of enemies
        enemyCount = 30;
//Debug Mode
        this.debug = false;

        localHighScoreService = new LocalHighScoreServiceProxy();
        globalHighScoreService = new GlobalHighScoreServiceProxy();
    }
    @Override
    public void show() {
// Initialize the camera for 2D rendering
        camera = new OrthographicCamera();
// Set up the viewport with the game's world dimensions and the camera
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
// Initialize the HUD camera for 2D rendering
        hudCamera = new OrthographicCamera();
// Set up the HUD viewport with the HUD dimensions and the HUD camera
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
// Initialize the ShapeRenderer for drawing shapes
        shapeRenderer = new ShapeRenderer();
// Load the default font
        font = ResourceManager.getInstance().getDefaultFont();
// Initialize the stage with the HUD viewport
        stage = new Stage(hudViewport);
// Create buttons and add them to the stage
        createButtons(stage, skin);
// Create game objects for the game
        createGameObjects();

    }
    private void createGameObjects() {
        gameManager = GameManager.getInstance();
          gameObjectFactory = new EGGameObjectFactory(
            new Rectangle(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT),
            touchpad);
// Create game objects using the factory and add them to the game manager
//Add the scrolling background which consist of 2 game object of same image
        GameObject screen1 = gameObjectFactory.createScrollingScreen(new Vector2(0, 0), new Vector2(0, -1f));
        screen1.setBounds(new Rectangle(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT));
        gameManager.addGameObject(screen1);
        GameObject screen2 = gameObjectFactory.createScrollingScreen(new Vector2(0, 16), new Vector2(0, -1f));
        screen2.setBounds(new Rectangle(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT));
        gameManager.addGameObject(screen2);
//Add Player
         player = (PlayerGameObject) gameObjectFactory.createPlayer(new Vector2(
            GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 5), Vector2.Zero);
        player.game = this;
        gameManager.addGameObject(player);
        player.setGameManager(gameManager);
//Add the alien enemies
        for (int i = 0; i < enemyCount; i++) {
            float offset = 0;
            offset = ((i / 5) % 2 == 0) ? 1.0f : 0.2f;
            float speed = -0.0001f - (float)Math.random() * 0.05f;
            GameObject enemy = gameObjectFactory.createEnemy(new Vector2(
                    offset + i % 5 * 1.7f,
                    13 + i / 5 * 2f),
                new Vector2(0, speed));
            enemy.setGameManager(gameManager);
            gameManager.addGameObject(enemy);
        }


    }
    public void createButtons(Stage stage, Skin skin){
        float btnWidth = 300, btnHeight = 80;
        UIFactory uif = new UIFactory(this.skin);
// Create Exit button
        playExit = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 + btnHeight * 6,
            btnWidth,btnHeight,"Exit");
// Add ChangeListener to the button
        playExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
// Exit Game
                invoker.setCommand(new ExitCommand());
                invoker.invoke();
            }
        });

//Create touchpad
        touchpad = (Touchpad) uif.createWidget(
            UIFactory.TOUCHPAD,
            20 ,
            20,
            200,200,"");
        Gdx.input.setInputProcessor(stage);
        stage.addActor(playExit);
// Create Fire Button
        fireButton = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 + btnHeight * 6,
            btnWidth,btnHeight,"FIRE");
        fireButton.setPosition(500, 100);
        fireButton.setSize(200, 80);
        fireButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isFiring = true;
                invoker.setCommand(new FireCommand(player));
                invoker.invoke();
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isFiring = false;
            }
        });
        Application.ApplicationType appType = Gdx.app.getType();
// Add touchpad only if it is android platform
        if (appType == Application.ApplicationType.Android) {
            stage.addActor(touchpad);
            stage.addActor(fireButton);
        }
        // For Highscore dialog
        Skin uiSkin = ResourceManager.getInstance().getAsset("default_skin/uiskin.json", Skin.class);
        final TextField textField = (TextField)uif.createWidget(UIFactory.TEXT_FIELD,0,0,400,50,"");
        dialog = new Dialog("Enter Player Name", uiSkin, "dialog") {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    String playerName = textField.getText();
                    localHighScoreService.saveHighScore(playerName, gameManager.getScore());
                    globalHighScoreService.saveHighScore(playerName, gameManager.getScore());
                    stage.getActors().removeValue(this, true);
                    gameManager.resetGame();
                    game.setScreen(new HighScoreScreen(game));
                }
            }
        };
        dialog.getContentTable().add(textField).width(400).row();
        dialog.padTop(50).padBottom(50); // Adjust padding if needed
        dialog.getButtonTable().padTop(20);
        dialog.button("OK", true);
        dialog.button("Cancel", false);

    }

    public void startFiring() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (isFiring) {
                    fire();
                } else {
                    cancel();
                }
            }
        }, 0, 0.5f); // Adjust the interval as needed
    }

    private void fire() {
// Implement your firing logic here
//Add Laser
        Vector2 pos = new Vector2();
        pos = player.getBounds().getCenter(pos);
        GameObject laser = gameObjectFactory.createBullet(pos,
            new Vector2(0,0.5f));
        gameManager.addGameObject(laser);
    }


    @Override
    public void render(float delta) {
// Clear the screen with a specific color
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
// Render the gameplay elements
        renderGamePlay(delta);
// Render the UI elements
        renderUI();
// Render the debug UI (e.g., grid)
        if (debug)
            renderDebugUI();
    }

    // Method to render the gameplay elements
    protected void renderGamePlay(float delta) {
        // Apply the viewport settings
        viewport.apply();
// Set the projection matrix for the SpriteBatch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if(!gameManager.isGameOver())
            gameManager.update(delta);
        else
        if(dialog.getStage()==null) dialog.show(stage);
        gameManager.update(delta);
        gameManager.render(batch);
        batch.end();

// Handle touchpad input
        float touchpadX = touchpad.getKnobPercentX();
        float touchpadY = touchpad.getKnobPercentY();
        System.out.println("Touchpad X: " + touchpadX + ", Y: " + touchpadY);
    }

    private void renderUI() {
        // Apply the HUD viewport settings
        hudViewport.apply();

        // Set the projection matrix for the SpriteBatch
        batch.setProjectionMatrix(hudCamera.combined);

        // Begin SpriteBatch
        batch.begin();

        // Draw the current score on the screen
        font.draw(batch, "Score: " + gameManager.getScore(), 10, GameConfig.HUD_HEIGHT - 10);

        // End SpriteBatch
        batch.end();

        // Draw the stage (for buttons, dialogs, etc.)
        stage.draw();
    }


    // Method to render the debug UI (e.g., grid)
    private void renderDebugUI() {
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, GameConfig.HUD_HEIGHT - 10);
        batch.end();
        ViewportUtils.getInstance().drawGrid(viewport, shapeRenderer);
    }
    @Override
    public void resize(int width, int height) {
// Update the viewport on window resize
        viewport.update(width, height);
// Update the HUD viewport on window resize
        hudViewport.update(width, height, true);
// Reposition the camera to the center of the screen
        camera.position.set(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2, 0);
    }


    @Override
    public void pause() {
// No specific actions needed on pause
    }
    @Override
    public void resume() {
// No specific actions needed on resume
    }
    @Override
    public void hide() {
// No specific actions needed on hide
        if (bgmGame != null && bgmGame.isPlaying()) {
            bgmGame.pause();  // Or you can use bgmGame.pause() if you want to resume it later
        }
    }
    @Override
    public void dispose() {
// Dispose of the background music if it was initialized
        if (bgmGame != null) {
            bgmGame.dispose();
        }
// Dispose of the ShapeRenderer to free up resources
        shapeRenderer.dispose();
// Dispose of the SpriteBatch to free up resources
        batch.dispose();
// Dispose of the font to free up resources
        font.dispose();
// Dispose of the GameManager
        gameManager.dispose();
    }
}
