package my.games.earthguardian.managers;

import com.badlogic.gdx.assets.AssetManager; // Importing AssetManager to manage game assets
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle; // Importing FileHandle to handle file operations
import com.badlogic.gdx.graphics.Texture; // Importing Texture to handle image files
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException; // Importing GdxRuntimeException to handle runtime exceptions
import com.badlogic.gdx.Gdx; // Importing Gdx to access LibGDX core functionalities

import java.awt.Font;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    // Singleton instance of Resource Manager
    private static ResourceManager instance;

    //Asset Manager instance to manage game assets
    private AssetManager assetManager;

    //Total number of assets to load
    private int totalAssets;

    //Number of assets loaded
    private int assetsLoaded;

    private ResourceManager(){
        assetManager = new AssetManager();// initializing Asset manager
        totalAssets = 0; // initializing total assets counts
        assetsLoaded = 0; // initializing loaded assets count
    }

    //Public method to provide access to the Singleton class
    public static ResourceManager getInstance(){
        if (instance == null){
            instance = new ResourceManager();
        }
        return instance;
    }

    //Method to load all assets listed in the assets.txt file
    public void loadAllAssets(String assetsFilePath){
        FileHandle fileHandle = Gdx.files.internal(assetsFilePath);
        if(fileHandle.exists() && !fileHandle.isDirectory()){ //checking if the files exist and is not a directory
            List<String> assetPaths = readAssetPaths(fileHandle); //read asset paths from the file
            totalAssets = assetPaths.size();
            loadAssets(assetPaths);
        }else{
            System.out.println("File does not exist or is a directory: "+ assetsFilePath);
        }
    }
    // Method to read asset paths from the assets.txt file
    private List<String> readAssetPaths(FileHandle fileHandle) {
        List<String> assetPaths = new ArrayList<>();
        String fileContent = fileHandle.readString(); // Read the content of the file
        String[] lines = fileContent.split("\n"); // Split the content into lines
        for (String line : lines) {
            line = line.trim(); // Trim whitespace from the line
            if (!line.isEmpty()) {
                assetPaths.add(line); // Add the line to the list of asset paths
            }
        }
        return assetPaths; // Return the list of asset paths
    }
    // Method to load assets from the list of asset paths
    private void loadAssets(List<String> assetPaths) {
        for (String path : assetPaths) {
            try {
                if (path.endsWith(".png")) { // Checking if the file is a PNG image
                    assetManager.load(path, Texture.class); // Loading the PNG image as a Texture
                } else if (path.endsWith(".mp3")) { // Checking if the file is a MP3 audio
                    assetManager.load(path, Music.class); // Loading the MP3 audio as a Music
                }else if (path.endsWith(".atlas")){
                    assetManager.load(path, TextureAtlas.class);
                    String newpath = path.replace(".atlas", ".json");
                    assetManager.load(newpath, Skin.class, new SkinLoader.SkinParameter(path));
                }else if(path.endsWith(".json")){
                    assetManager.load(path, Skin.class);
                }
// Add more conditions for other asset types if needed
            } catch (GdxRuntimeException e) { // Catching any runtime exceptions during asset loading
                System.err.println("Failed to load asset: " + path); // Printing an error message if asset loading fails
            }
        }
    }
    // Method to block until all assets are loaded
    public boolean finishLoading() {
        boolean allLoaded = assetManager.update(); // Updating the asset loading process
        if (allLoaded) {
            assetsLoaded = totalAssets; // All assets are loaded
        } else {
            assetsLoaded = (int) (totalAssets * assetManager.getProgress()); // Updating the number of loaded assets
        }
        return allLoaded; // Returning true if all assets are loaded, false otherwise
    }
    // Generic method to get a loaded asset by its file path and type
    public <T> T getAsset(String filePath, Class<T> type) {
        return assetManager.get(filePath, type); // Returning the loaded asset
    }

    // Method to check if a specific asset is loaded
    public boolean isAssetLoaded(String filePath) {
        return assetManager.isLoaded(filePath); // Checking if the asset is loaded
    }
    // Method to check the progress of asset loading
    public float getLoadingProgress() {
        return (float) assetsLoaded / totalAssets; // Returning the progress as a float between 0 and 1
    }

    public BitmapFont getDefaultFont(){

        String fontPath = "Fonts/SpaceQuest.ttf";
        String fontName = "SpaceQuest";

        if (isAssetLoaded(fontName)){
            return getAsset(fontName, BitmapFont.class);
        }

        FileHandle fontFileHandle = Gdx.files.internal(fontPath);
        if(!fontFileHandle.exists()){
            System.err.println("Font file does not exist: "+ fontPath);
            return null;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFileHandle);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

        BitmapFont font = generator.generateFont(parameter);
        assetManager.load(fontName, BitmapFont.class);
        generator.dispose();
        return font;
    }
    // Method to dispose of all loaded assets
    public void dispose() {
        assetManager.dispose(); // Disposing of all loaded assets
    }

}
