package my.games.earthguardian.commands;

import com.badlogic.gdx.math.Vector2;

import my.games.earthguardian.gameobjects.PlayerGameObject;

public class MoveCommand implements Command{
    private PlayerGameObject player;
    private Vector2 direction;

    public MoveCommand(PlayerGameObject player, Vector2 direction) {
        this.player = player;
        this.direction = direction;
    }
    @Override
    public void execute() {
        player.move(direction);
    }
}
