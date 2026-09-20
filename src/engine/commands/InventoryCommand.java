package engine.commands;

import engine.model.Item;
import engine.model.Player;

import java.util.List;

public class InventoryCommand implements Command {
    @Override
    public void execute(Player player, String argument) {
        List<Item> inventory = player.getInventory();

        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        }

        System.out.println("You are carrying:");
        for (Item item : inventory) {
            System.out.println("  - " + item.getName() + ": " + item.getDescription());
        }
    }
}