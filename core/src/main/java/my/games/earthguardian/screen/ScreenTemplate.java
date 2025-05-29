package my.games.earthguardian.screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import my.games.earthguardian.EarthGuardianGame;
import my.games.earthguardian.config.GameConfig;
import my.games.earthguardian.managers.ResourceManager;
import my.games.earthguardian.utils.ViewportUtils;

public abstract class ScreenTemplate implements Screen {
    protected final EarthGuardianGame game;
    protected OrthographicCamera camera;
    protected OrthographicCamera hudCamera;
    protected Viewport viewport;
    protected Viewport hudViewport;
    protected SpriteBatch batch;
    protected Stage stage;
    protected static Skin skin;
    protected Texture background;
    protected static BitmapFont font;
    protected ShapeRenderer shapeRenderer;
    protected boolean debug = false; // Debug variable moved here
    protected static boolean assetsLoaded = false; // Static flag to ensure assets are loaded only once
    public ScreenTemplate(EarthGuardianGame game) {
        this.game = game;
        this.batch = game.getBatch();
    }

    @Override
    public void show(){
        loadAssets();
        setupCameras();
        setupViewport();
        setupStage();
    }

    protected void loadAssets() {
        if (!assetsLoaded) {
            ResourceManager.getInstance().loadAllAssets("assets.txt");
            assetsLoaded = true;
            skin = new Skin(Gdx.files.internal("skins/neon-ui.json"));
            font = ResourceManager.getInstance().getDefaultFont();
        }
    }
    protected void setupCameras() {
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
    }
    protected void setupViewport() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
    }
    protected void setupStage() {
        shapeRenderer = new ShapeRenderer();
        stage = new Stage(hudViewport);
    }
    protected abstract void renderGamePlay(float delta);

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        hudViewport.update(width, height, true);
        camera.position.set(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        renderGamePlay(delta);
        stage.draw();
        if (debug) {
            renderDebugInfo();
        }
    }
    protected void renderDebugInfo() {
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, GameConfig.HUD_HEIGHT - 10);
        batch.end();
        ViewportUtils.getInstance().drawGrid(viewport, shapeRenderer);
    }
    @Override
    public void pause() {
// Default implementation (can be overridden in subclasses if needed)
    }
    @Override
    public void resume() {
// Default implementation (can be overridden in subclasses if needed)
    }

    @Override
    public void hide() {
// Default implementation (can be overridden in subclasses if needed)
    }
    @Override
    public void dispose() {
        if (background != null) {
            background.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
