package engine.commands;

import engine.model.Item;
import engine.model.NPC;
import engine.model.Player;
import engine.model.Room;

import java.util.List;

public class LookCommand implements Command {
    @Override
    public void execute(Player player, String direction) {
        Room room = player.getRoom();
        System.out.println("\n" + room.getName());


        if (room.getDarkDescription() != null && !room.isLit()) {
            System.out.println(room.getDarkDescription());
            return;
        }

        System.out.println(room.getDescription());

        // items
        List<Item> items = room.getStorage();
        if (items != null && !items.isEmpty()) {
            System.out.println("\nYou see:");
            for (Item item : items) {
                System.out.println("  - " + item.getName() + ": " + item.getDescription());
            }
        }

        // npcs
        List<NPC> npcs = room.getNpcs();
        if (npcs != null && !npcs.isEmpty()) {
            System.out.println("\nPeople here:");
            for (NPC npc : npcs) {
                System.out.println("  - " + npc.getName());
            }
        }

        // exits
        System.out.println("\nExits:");
        if (room.getNorth() != null) System.out.println("  - North");
        if (room.getSouth() != null) System.out.println("  - South");
        if (room.getEast() != null) System.out.println("  - East");
        if (room.getWest() != null) System.out.println("  - West");
        if (room.getNorth() == null && room.getSouth() == null &&
                room.getEast() == null && room.getWest() == null) {
            System.out.println("  None");
        }
    }
}