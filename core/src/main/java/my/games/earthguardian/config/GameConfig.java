package my.games.earthguardian.config;

public class GameConfig {
    private static GameConfig instance;

    private GameConfig(){

    }

    public static GameConfig getInstance(){
        if(instance == null){
            instance = new GameConfig();
        }
        return instance;
    }

    // Configuration constants for the game

    // Width of the desktop application window
    public static final float WIDTH = 720f;

    //Height of the desktop application window
    public static final float HEIGHT = 720f;

    //Width of the HUD (Heads-up Display) in the world units
    public static final float HUD_WIDTH = 720f;

    //Height of the HUD (Heads-up Display) in the world units
    public static final float HUD_HEIGHT = 1280f;

    //Width of the game world in world units
    public static final float WORLD_WIDTH = 9f;

    //Height of the world in world units
    public static final float WORLD_HEIGHT = 16f;

    // X-coordinate of the center of the game world
    public static final float WORLD_CENTER_X = WORLD_WIDTH/2;

    //Y-coordinate of the center of the game world
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT/2;


}
