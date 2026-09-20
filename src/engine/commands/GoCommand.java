package engine.commands;

import engine.model.Item;
import engine.model.Player;
import engine.model.Room;

public class GoCommand implements Command {
    @Override
    public void execute(Player player, String direction) {
        Room current = player.getRoom();
        Room nextRoom = switch (direction.toLowerCase()) {
            case "north" -> current.getNorth();
            case "south" -> current.getSouth();
            case "east"  -> current.getEast();
            case "west"  -> current.getWest();
            default      -> null;
        };

        if (nextRoom == null) {
            System.out.println("You can't go that way.");
            return;
        }

        if (nextRoom.hasCondition() &&
                nextRoom.getConditionType().equals("requires_item") &&
                nextRoom.getConditionFailMessage() != null) {

            boolean hasItem = false;
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase(nextRoom.getConditionItem())) {
                    hasItem = true;
                    break;
                }
            }
            if (!hasItem) {
                System.out.println(nextRoom.getConditionFailMessage());
                System.exit(0);
                return;
            }
        }

        player.setRoom(nextRoom);
        new LookCommand().execute(player, "");
    }
}