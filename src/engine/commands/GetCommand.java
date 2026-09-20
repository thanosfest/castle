package engine.commands;

import engine.model.Item;
import engine.model.Player;
import engine.model.Room;

import java.util.List;

public class GetCommand implements Command {
    @Override
    public void execute(Player player, String itemName) {
        Room room = player.getRoom();

        if (room.hasCondition() && room.getDarkDescription() != null && !room.isLit()) {
            System.out.println("It is too dark to find anything.");
            return;
        }

        List<Item> storage = room.getStorage();
        Item found = null;
        for (Item item : storage) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                found = item;
                break;
            }
        }

        if (found == null) {
            System.out.println("There is no " + itemName + " here.");
        } else {
            storage.remove(found);
            player.addItem(found);
            System.out.println("You picked up: " + found.getName());
        }
    }
}