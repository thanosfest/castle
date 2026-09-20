package engine.commands;

import engine.model.NPC;
import engine.model.Player;
import engine.model.Room;

public class SpeakCommand implements Command {
    @Override
    public void execute(Player player, String argument) {
        if (argument.startsWith("to ")) {
            argument = argument.substring(3).trim();
        }

        if (argument.isEmpty()) {
            System.out.println("Speak to whom?");
            return;
        }

        Room room = player.getRoom();
        NPC npc = room.getNpcByName(argument);

        if (npc == null) {
            System.out.println("There is no " + argument + " here.");
            return;
        }

        System.out.println(npc.getName() + ": " + npc.speak(player.getName()));
    }
}