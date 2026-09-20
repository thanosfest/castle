package engine.commands;

import engine.model.Player;

public interface Command {
    void execute(Player player,String argument);
}