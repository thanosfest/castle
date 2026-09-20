package engine.commands;

import engine.model.Item;
import engine.model.NPC;
import engine.model.Player;
import engine.model.Room;

import java.util.List;
import java.util.Map;

public class UseCommand implements Command {
    private final Map<String, Room> roomMap;

    public UseCommand(Map<String, Room> roomMap) {
        this.roomMap = roomMap;
    }

    @Override
    public void execute(Player player, String argument) {
        String[] parts = argument.split(" on ", 2);
        String itemName = parts[0].trim();
        String target = parts.length > 1 ? parts[1].trim() : "";

        List<Item> inventory = player.getInventory();

        Item found = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                found = item;
                break;
            }
        }

        if (found == null) {
            System.out.println("You don't have a " + itemName + ".");
            return;
        }

        String effect = found.getEffect();
        String message = found.getMessage();

        if (effect == null || effect.equals("none")) {
            System.out.println("Nothing happens.");
            return;
        }

        String[] effectParts = effect.split(":");
        switch (effectParts[0]) {
            case "unlock" -> {
                String direction = effectParts[1];
                String roomId = effectParts[2];
                String requiredTarget = effectParts.length > 3 ? effectParts[3] : "";

                // player typed "use nail" without a target but one is required
                if (!requiredTarget.isEmpty() && target.isEmpty()) {
                    System.out.println("Use " + found.getName() + " on what?");
                    return;
                }

                if (!requiredTarget.isEmpty() && !target.equalsIgnoreCase(requiredTarget)) {
                    System.out.println("You can't do that");
                    return;
                }

                roomMap.get(roomId).unlock(direction);
                System.out.println(message != null ? message : "Something unlocked.");
            }
            case "light" -> {
                String roomId = effectParts[1];
                roomMap.get(roomId).setLit(true);
                inventory.remove(found);
                System.out.println(message != null ? message : "The room is illuminated.");
            }
            case "none" -> {
                // check if we are in a dark room and this item matches the required light source
                Room room = player.getRoom();
                if (room.hasCondition() &&
                        room.getConditionType().equals("requires_item") &&
                        room.getDarkDescription() != null &&
                        found.getName().equalsIgnoreCase(room.getConditionItem())) {
                    System.out.println("You hold up the " + found.getName() + ". The room is illuminated.");
                    new LookCommand().execute(player, "");
                } else {
                    System.out.println("Nothing happens.");
                }
            }
            case "win" -> {
                if (effectParts.length > 1) {
                    String targetName = effectParts[1];
                    Room room = player.getRoom();
                    NPC npc = room.getNpcByName(targetName);

                    if (npc == null) {
                        System.out.println("There is no one to fight here.");
                        return;
                    }

                    if (!npc.hasSpoken()) {
                        System.out.println("You should confront " + npc.getName() + " first.");
                        return;
                    }
                }

                System.out.println(message);
                System.exit(0);
            }
            default -> System.out.println("Nothing happens.");
        }
    }
}