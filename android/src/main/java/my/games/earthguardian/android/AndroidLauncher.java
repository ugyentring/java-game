package my.games.earthguardian.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import my.games.earthguardian.EarthGuardianGame;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        EarthGuardianGame game = new EarthGuardianGame();
        game.setSettingsAdapter(new AndroidSettingsAdapter(this));
        initialize(game, configuration);

    }
}
