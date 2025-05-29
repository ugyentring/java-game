package my.games.earthguardian.android;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import my.games.earthguardian.services.SettingsAdapter;

public class AndroidSettingsAdapter implements SettingsAdapter {
    private Context context;

    public AndroidSettingsAdapter(Context context){
        this.context = context;

    }

    @Override
    public void launchSettings(){
        //Create an intent to launch the Android settings
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }
}
