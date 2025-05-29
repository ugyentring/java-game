package my.games.earthguardian.lwjgl3;

import javax.swing.JOptionPane;

import my.games.earthguardian.services.SettingsAdapter;

public class DesktopSettingsAdapter implements SettingsAdapter {
    @Override
    public void launchSettings(){
        //Show a message box indicating settings launch is not supported
        JOptionPane.showMessageDialog(null, "Settings launch not supported on Desktop", "Settings", JOptionPane.INFORMATION_MESSAGE);
    }
}
