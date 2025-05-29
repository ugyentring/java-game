package my.games.earthguardian.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Font;

import my.games.earthguardian.EarthGuardianGame;
import my.games.earthguardian.config.GameConfig;
import my.games.earthguardian.managers.ResourceManager;
import my.games.earthguardian.utils.UIFactory;
import my.games.earthguardian.utils.ViewportUtils;

public class MenuScreen extends ScreenTemplate {
    // UI elements
    private TextButton playButton;
    private TextButton playHighScore;
    private TextButton playExit;
    private Skin skin;
    private Stage stage;
    // Graphics and rendering
    private Texture background;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    // Cameras and viewports
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Viewport viewport;
    private Viewport hudViewport;
    private TextButton settingButton;
    // Audio
    private Music bgmMenu;
    // Flags
    private boolean debug;
    // Constructor to initialize the GameScreen with the main game reference
    public MenuScreen(EarthGuardianGame game) {
        super(game);
// Load the splash screen background image
        this.background = ResourceManager.getInstance().getAsset("images/menu.png", Texture.class);
// Retrieve and play the background music if not already initialized
        bgmMenu = ResourceManager.getInstance().getAsset("sounds/music/menu_music.mp3", Music.class);
        bgmMenu.setLooping(true);
        bgmMenu.play();
// Get the SpriteBatch from the game
        this.batch = game.getBatch();
// Load the skin (UI elements)
        skin = ResourceManager.getInstance().getAsset("skins/neon-ui.json", Skin.class);
        this.debug = false;
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
        stage = new Stage(hudViewport);
        createButtons(stage, skin);
    }
    public void createButtons(Stage stage, Skin skin){
        float btnWidth = 300, btnHeight = 80;
        UIFactory uif = new UIFactory(this.skin);
// Create Play button
        playButton = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 - btnHeight * 2,
            btnWidth,btnHeight,"Play");
// Add ChangeListener to the button
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//stop BGM
                bgmMenu.stop();
// Start New Game
                game.setScreen(new GameScreen(game));
            }
        });
// Create High Score button
        playHighScore = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 - btnHeight * 3,
            btnWidth,btnHeight,"Scores");

        //Add Change listener to the button
        playHighScore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //stop BGM
                bgmMenu.stop();
                game.setScreen(new HighScoreScreen(game));
            }
        });
// Create Exit button
        playExit = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 - btnHeight * 4,
            btnWidth,btnHeight,"Exit");
// Add ChangeListener to the button
        playExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
// Exit Game
                Gdx.app.exit();
            }
        });

        // Create Setting button
        settingButton = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 - btnHeight * 5,
            btnWidth,btnHeight,"Setting");
// Add ChangeListener to the button
        settingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
// Open Setting
                game.openSettings();
            }
        });

        Gdx.input.setInputProcessor(stage);
        stage.addActor(playButton);
        stage.addActor(playHighScore);
        stage.addActor(settingButton);
        stage.addActor(playExit);

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
// Draw the background image
        batch.begin();
        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.end();
    }
    // Method to render the UI elements
    private void renderUI() {
// Apply the HUD viewport settings
        hudViewport.apply();
// Set the projection matrix for the SpriteBatch
        batch.setProjectionMatrix(hudCamera.combined);
// Draw the buttons
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
    }
    @Override
    public void dispose() {
// Dispose of the background texture to free up resources
        background.dispose();
// Dispose of the background music if it was initialized
        if (bgmMenu != null) {
            bgmMenu.dispose();
        }
// Dispose of the ShapeRenderer to free up resources
        shapeRenderer.dispose();
// Dispose of the SpriteBatch to free up resources
        batch.dispose();
// Dispose of the font to free up resources
        font.dispose();
    }
}
