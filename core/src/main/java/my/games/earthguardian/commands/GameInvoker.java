package my.games.earthguardian.commands;

public class GameInvoker {
    private Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void invoke() {
        if (command != null) {
            command.execute();
        }
        }
}
