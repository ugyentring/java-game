package my.games.earthguardian.utils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;

// Utility class for viewport-related tasks
// Uses the singleton pattern to ensure a single instance
public class ViewportUtils {

    //logger for debugging purposes
    private static final Logger log = new Logger(ViewportUtils.class.getName(), Logger.DEBUG);

    // Default cell size for the grid
    private static final int DEFAULT_CELL_SIZE = 1;

    //Static instance of the class
    private static ViewportUtils instance;

    //Private constructor to prevent instantiation
    private ViewportUtils(){

    }

    // public method to provide access to the single instance of ViewportUtils
    // if the instance does not exist, it is created. Otherwise, the existing instance is returned.
    // @return the single instance of ViewportUtils

    public static ViewportUtils getInstance(){
        if(instance == null){
            instance = new ViewportUtils();
        }
        return instance;
    }

    // Draws a grid on the paper using the default cell size
    //@param viewport the viewport to draw the grid on
    //@param renderer the ShapeRenderer used to draw the grid

    public void drawGrid(Viewport viewport, ShapeRenderer renderer){
        drawGrid(viewport, renderer, DEFAULT_CELL_SIZE);
    }

    //Draws a grid on the viewport with the specified cell size
    // @param viewport the viewport to draw the grid on
    // @param renderer the ShapeRenderer used to draw on the grid
    // @param cellSize the size of each cell in the grid



    public void drawGrid(Viewport viewport, ShapeRenderer renderer, int cellSize) {
    // Validate parameters/arguments
        if (viewport == null) {
            throw new IllegalArgumentException("viewport param is required.");
        }
        if (renderer == null) {
            throw new IllegalArgumentException("renderer param is required.");
        }
        if (cellSize < DEFAULT_CELL_SIZE) {
            cellSize = DEFAULT_CELL_SIZE;
        }
    // Copy old color from renderer
        Color oldColor = new Color(renderer.getColor());
        int worldWidth = (int) viewport.getWorldWidth();
        int worldHeight = (int) viewport.getWorldHeight();
        int doubleWorldWidth = worldWidth * 2;
        int doubleWorldHeight = worldHeight * 2;
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        // Draw vertical lines
        for (int x = -doubleWorldWidth; x < doubleWorldWidth; x += cellSize) {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight);
        }
    // Draw horizontal lines
        for (int y = -doubleWorldHeight; y < doubleWorldHeight; y += cellSize) {
            renderer.line(-doubleWorldWidth, y, doubleWorldWidth, y);
        }
    // Draw x-y axis lines
        renderer.setColor(Color.RED);
        renderer.line(0, -doubleWorldHeight, 0, doubleWorldHeight);
        renderer.line(-doubleWorldWidth, 0, doubleWorldWidth, 0);
    // Draw world bounds
        renderer.setColor(Color.GREEN);
        renderer.line(0, worldHeight, worldWidth, worldHeight);
        renderer.line(worldWidth, 0, worldWidth, worldHeight);
        renderer.end();
    // Restore old color
        renderer.setColor(oldColor);
    }

    //Logs the pixels per unit (PPU) ratio for the viewport
    //@param viewport the viewport to debug

    public void debugPixelPerUnit(Viewport viewport){
        if(viewport ==null){
            throw new IllegalArgumentException("viewport param is required");
        }

        float screenWidth = viewport.getScreenWidth();
        float screenHeight = viewport.getScreenHeight();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        //PPU => pixels per world unit
        float xPPU = screenWidth / worldWidth;
        float yPPU = screenHeight / worldHeight;

        log.debug("x PPU=" + xPPU+ "yPPU=" + yPPU);
    }

}
