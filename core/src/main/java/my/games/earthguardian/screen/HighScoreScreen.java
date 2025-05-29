package my.games.earthguardian.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import my.games.earthguardian.EarthGuardianGame;
import my.games.earthguardian.config.GameConfig;
import my.games.earthguardian.managers.ResourceManager;
import my.games.earthguardian.services.GlobalHighScoreServiceProxy;
import my.games.earthguardian.services.HighScore;
import my.games.earthguardian.services.HighScoreService;
import my.games.earthguardian.services.LocalHighScoreServiceProxy;
import my.games.earthguardian.utils.UIFactory;

public class HighScoreScreen extends ScreenTemplate {

    //Sprite Batch from drawing textures
    private final SpriteBatch batch;

    //Camera for the game world
    private OrthographicCamera camera;

    //Camera for the HUD(Head-Up Display)
    private OrthographicCamera hudCamera;

    //Viewport for the game world
    private Viewport viewport;

    //Viewport for the HUD
    private Viewport hudViewport;

    //Stage for managing Ui Elements
    private Stage stage;

    //Skin for UI Elements
    private Skin skin;

    //Service for managing local high scores
    private HighScoreService localHighScoreService;

    //Service for managing global high scores
    private HighScoreService globalHighScoreService;

    //Factory for creating UI elements
    private UIFactory uiFactory;

    // Texture for the screen background
    private Texture background;

    //Music for the splash screen background
    private Music bgmSplash;

    //BitmapFont for rendering text
    private BitmapFont font;



    public HighScoreScreen( EarthGuardianGame game) {
        super(game);
// Load the splash screen background image
        this.background = ResourceManager.getInstance().getAsset("images/space_bgd.png", Texture.class);
// Retrieve and play the background music if not already initialized
        bgmSplash = ResourceManager.getInstance().getAsset("sounds/music/menu_music.mp3", Music.class);
        bgmSplash.setLooping(true);
        bgmSplash.play();
// Get the SpriteBatch from the game
        this.batch = game.getBatch();
// Load the skin (UI elements)
        skin = ResourceManager.getInstance().getAsset("skins/neon-ui.json", Skin.class);
        uiFactory = new UIFactory(skin);
// Load the default font
        font = ResourceManager.getInstance().getDefaultFont();

        //Instantiate LocalHighScoreService here
        localHighScoreService = new LocalHighScoreServiceProxy();

        //Instantiate GlobalHighScoreService here
        globalHighScoreService =  new GlobalHighScoreServiceProxy();

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
// Initialize the stage with the HUD viewport
        stage = new Stage(hudViewport);
// Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
// Set up the UI elements
        setupUI();
    }

    private void setupUI() {
// Create a table for layout
        Table table = (Table) uiFactory.createWidget(UIFactory.TABLE, 0, 0, 400, 200, null);
// Make the table fill the parent container
        table.setFillParent(true);
// Add the table to the stage
        stage.addActor(table);
// Create labels for local and global high scores
        Label localLabel = (Label) uiFactory.createWidget(UIFactory.LABEL, 0, 0, 0, 0, "Local High Scores");
        Label globalLabel = (Label) uiFactory.createWidget(UIFactory.LABEL, 0, 0, 0, 0, "Global High Scores");
// Create lists for displaying local and global high scores
        List<String> localList = (List<String>) uiFactory.createWidget(UIFactory.LIST, 0, 0, 0, 0, null);
        List<String> globalList = (List<String>) uiFactory.createWidget(UIFactory.LIST, 0, 0, 0, 0, null);
// Create scroll panes for the high score lists
        ScrollPane localScrollPane = (ScrollPane) uiFactory.createWidget(UIFactory.SCROLL_PANE, 0, 0, 0, 0, null);
        localScrollPane.setActor(localList);
        ScrollPane globalScrollPane = (ScrollPane) uiFactory.createWidget(UIFactory.SCROLL_PANE, 0, 0, 0, 0, null);
        globalScrollPane.setActor(globalList);
// Add the labels and scroll panes to the table
        table.add(localLabel).expandX().padTop(100);
        table.row();
        table.add(localScrollPane).expand().fill().pad(20);
        table.row();
        table.add(globalLabel).expandX().padTop(50);
        table.row();
        table.add(globalScrollPane).expand().fill().pad(20);
        table.row();

        //high scores into the lists
        loadHighScores(localList, globalList);

        //button dimensions
        float btnWidth = 300, bthHeight = 80;

        //Create a new UI Factory instance with the skin
        UIFactory uif = new UIFactory(this.skin);

        //Create Exit Button
        TextButton playExit = (TextButton) uif.createWidget(
            UIFactory.BUTTON,
            GameConfig.HUD_WIDTH / 2 - btnWidth / 2,
            GameConfig.HUD_HEIGHT / 2 - bthHeight * 8,
            btnWidth,
            bthHeight,
            "Exit"
        );

        // Add ChangeListener to the button
        playExit.addListener((new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Exit Game to Main menu
                game.setScreen(new MenuScreen(game));
            }
        }));

        // Add the Exit Button to the table
        // Add the Exit Button to the table
        table.add(playExit).fill().padBottom(100);
    }

    private void loadHighScores(List<String> localList, List<String> globalList) {
        java.util.List<HighScore> localScores = localHighScoreService.loadTopTenScores();
        String[] localArray = new String[localScores.size()];
        for (int i = 0; i < localScores.size(); i++) {
            HighScore score = localScores.get(i);
            localArray[i] = score.getName() + ": " + score.getScore();
        }
// Set the items of the local high score list to the array of strings
        localList.setItems(localArray);

        java.util.List<HighScore> globalScores = globalHighScoreService.loadTopTenScores();
        String[] globalArray = new String[globalScores.size()];
        for (int i = 0; i < globalScores.size(); i++) {
            HighScore score = globalScores.get(i);
            globalArray[i] = score.getName() + ": " + score.getScore();
        }
        globalList.setItems(globalArray);
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
        );

        renderGamePlay(delta);
        renderUI();
    }

    protected void renderGamePlay(float delta){
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0 , 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.end();
    }

    //Method to render the UI elements
    private void renderUI(){
        hudViewport.apply();

        batch.setProjectionMatrix(hudCamera.combined);

        stage.draw();
        batch.begin();

        font.draw(batch, "HIGH SCORES", 200, GameConfig.HUD_HEIGHT - 50);
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        //update the viewport on window size
        viewport.update(width, height);

        //update the HUD viewport on window size
        hudViewport.update(width, height, true);

        //Reposition the camera to the center of the screen
        camera.position.set(GameConfig.WORLD_WIDTH /2, GameConfig.WORLD_HEIGHT /2, 0);


    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void hide(){

    }

    @Override
    public void dispose(){
        stage.dispose();
        skin.dispose();
    }


}
