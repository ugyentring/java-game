package my.games.earthguardian;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import my.games.earthguardian.screen.SplashScreen;
import my.games.earthguardian.services.SettingsAdapter;

public class EarthGuardianGame  extends Game {
    private SpriteBatch batch;
    private Texture image;
    private Music bgmSplash;
    private AssetManager assetManager;
    private SettingsAdapter settingsAdapter;

    @Override
    public void create(){
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        image = new Texture("images/splash.png");
        assetManager.load("sounds/music/EarthGuardian.mp3", Music.class);
        setScreen(new SplashScreen(this));
    }

    public void setSettingsAdapter(SettingsAdapter settingsAdapter){
        this.settingsAdapter = settingsAdapter;
    }

    @Override
    public void render(){
        super.render();
    }

    @Override
    public void dispose(){
        batch.dispose();
        image.dispose();
        assetManager.dispose();
        if(bgmSplash !=null){
            bgmSplash.dispose();
        }
    }

    public SpriteBatch getBatch(){
        return batch;
    }

    public AssetManager getAssetManager(){
        return assetManager;
    }

    public void openSettings() {
        if (settingsAdapter != null) {
            settingsAdapter.launchSettings();
        } else {
            System.out.println("SettingsAdapter not set.");
        }
    }
}
