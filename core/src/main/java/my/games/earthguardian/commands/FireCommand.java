package my.games.earthguardian.commands;

import my.games.earthguardian.gameobjects.PlayerGameObject;

public class FireCommand implements Command {
    private PlayerGameObject player;
    public FireCommand(PlayerGameObject player) {
        this.player = player;
    }
    @Override
    public void execute() {
        player.startFiring();
    }
}
