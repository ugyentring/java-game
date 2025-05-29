package my.games.earthguardian.commands;

import com.badlogic.gdx.Gdx;

public class ExitCommand implements Command{
    @Override
    public void execute() {
        Gdx.app.exit();
    }
}
