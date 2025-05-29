package my.games.earthguardian.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Menu;

import my.games.earthguardian.EarthGuardianGame;
import my.games.earthguardian.config.GameConfig;
import my.games.earthguardian.managers.ResourceManager;
import my.games.earthguardian.utils.ViewportUtils;

public class SplashScreen extends ScreenTemplate {
    private Texture background;
    private Music bgmSplash;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private boolean loadingComplete;
    private boolean debug;
    private SpriteBatch batch;
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;
    private BitmapFont font;
    private float elapsedTime;

    public SplashScreen(EarthGuardianGame game) {
        super(game);
        this.background = new Texture("images/splash.png");
        this.batch = game.getBatch();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);

        ResourceManager.getInstance().loadAllAssets("assets.txt");
        font = loadDefaultFont();
        elapsedTime = 0;
    }

    private BitmapFont loadDefaultFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/SpaceQuest.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderWidth = 5;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.YELLOW;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = new Color(0, 0.5f, 0, 0.75f);
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        if (ResourceManager.getInstance().finishLoading()) {
            if (!loadingComplete) {
                loadingComplete = true;
                if (bgmSplash == null) {
                    bgmSplash = ResourceManager.getInstance().getAsset("sounds/music/EarthGuardian.mp3", Music.class);
                    bgmSplash.setLooping(true);
                    bgmSplash.play();
                }
            }
        }

        if (loadingComplete && Gdx.input.isTouched()) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        renderGamePlay(delta);
        renderUI();
        renderDebugUI();
    }

    protected void renderGamePlay(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.end();

    }

    private void renderUI() {
        int fps = Gdx.graphics.getFramesPerSecond();
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        float progress = ResourceManager.getInstance().getLoadingProgress();
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.25f, 0.25f, 0.25f, 1);
        shapeRenderer.rect(100, GameConfig.HEIGHT / 2 + 50, GameConfig.WIDTH - 200, 50);
        shapeRenderer.setColor(1f, 0.75f, 0.75f, 1);
        shapeRenderer.rect(100, GameConfig.HEIGHT / 2 + 50, (GameConfig.WIDTH - 200) * progress, 50);
        shapeRenderer.end();

        batch.begin();
        elapsedTime += Gdx.graphics.getDeltaTime();  // Update elapsed time
        float alpha = Math.abs(MathUtils.sin(elapsedTime * 2f));
        if (loadingComplete) {
            font.setColor(1, 1, 1, alpha);
            font.draw(batch, "TAP TO CONTINUE", GameConfig.WIDTH / 6, GameConfig.HEIGHT / 3 * 2);
            font.setColor(Color.WHITE);
            font.draw(batch, "FPS: " + fps, GameConfig.WIDTH / 20, GameConfig.HEIGHT * 1.75f);
        } else {
            font.draw(batch, "LOADING ASSETS", GameConfig.WIDTH / 6, GameConfig.HEIGHT / 3 * 2);
        }
        batch.end();
    }

    private void renderDebugUI() {
        ViewportUtils.getInstance().drawGrid(viewport, shapeRenderer);
        ViewportUtils.getInstance().debugPixelPerUnit(viewport);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudViewport.update(width, height, true);
        camera.position.set(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        if (bgmSplash != null && bgmSplash.isPlaying()) {
            bgmSplash.pause();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        if (bgmSplash != null) {
            bgmSplash.dispose();
        }
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
