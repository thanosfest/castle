package engine.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.model.Item;
import engine.model.NPC;
import engine.model.Player;
import engine.model.Room;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldLoader {

    public static WorldData loadFromJson(String resourcePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = new FileInputStream(resourcePath);
        JsonNode root = mapper.readTree(is);

        // 1. Build all rooms by id
        Map<String, Room> roomMap = new HashMap<>();
        for (JsonNode roomNode : root.get("rooms")) {
            String id = roomNode.get("id").asText();
            String name = roomNode.get("name").asText();
            String description = roomNode.get("description").asText();

            Room room = new Room(name, description);

            // Load items into room storage
            List<Item> storage = new ArrayList<>();
            if (roomNode.has("items")) {
                for (JsonNode itemNode : roomNode.get("items")) {
                    storage.add(new Item(
                            itemNode.get("name").asText(),
                            itemNode.get("description").asText(),
                            itemNode.has("effect")  ? itemNode.get("effect").asText()  : "none",
                            itemNode.has("message") ? itemNode.get("message").asText() : null
                    ));
                }
            }
            room.setStorage(storage);

            // Load NPCs
            List<NPC> npcs = new ArrayList<>();
            if (roomNode.has("npcs")) {
                for (JsonNode npcNode : roomNode.get("npcs")) {
                    String npcName = npcNode.get("name").asText();
                    List<String> dialogue = new ArrayList<>();
                    for (JsonNode line : npcNode.get("dialogue")) {
                        dialogue.add(line.asText());
                    }
                    npcs.add(new NPC(npcName, dialogue));
                }
            }
            room.setNpcs(npcs);

            // Load condition
            if (roomNode.has("condition")) {
                JsonNode cond = roomNode.get("condition");
                room.setCondition(
                        cond.has("type")        ? cond.get("type").asText()        : null,
                        cond.has("item")        ? cond.get("item").asText()        : null,
                        cond.has("failMessage") ? cond.get("failMessage").asText() : null
                );
                if (cond.has("darkDescription")) {
                    room.setDarkDescription(cond.get("darkDescription").asText());
                }
            }
            roomMap.put(id, room);
        }

        // 2. Wire up connections
        for (JsonNode conn : root.get("connections")) {
            Room from = roomMap.get(conn.get("from").asText());
            Room to   = roomMap.get(conn.get("to").asText());
            String direction = conn.get("direction").asText();
            boolean locked = conn.has("locked") && conn.get("locked").asBoolean();

            if (locked) {
                from.lockDirection(direction, to);
            } else {
                switch (direction) {
                    case "north" -> from.setNorth(to);
                    case "south" -> from.setSouth(to);
                    case "east"  -> from.setEast(to);
                    case "west"  -> from.setWest(to);
                }
            }
        }

        // 3. Build player inventory
        JsonNode playerNode = root.get("player");
        List<Item> inventory = new ArrayList<>();
        if (playerNode.has("inventory")) {
            for (JsonNode itemNode : playerNode.get("inventory")) {
                inventory.add(new Item(
                        itemNode.get("name").asText(),
                        itemNode.get("description").asText(),
                        itemNode.has("effect")  ? itemNode.get("effect").asText()  : "none",
                        itemNode.has("message") ? itemNode.get("message").asText() : null
                ));
            }
        }

        // 4. Create player and return WorldData
        Room startRoom = roomMap.get(playerNode.get("startingRoom").asText());
        Player player = new Player(inventory, startRoom, playerNode.get("name").asText());

        WorldData data = new WorldData();
        data.player = player;
        data.rooms = roomMap;
        return data;
    }
}