package engine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
public class Room {
    private String name;
    private String description;
    private List<Item> storage;
    private Map<String, Room> lockedConnections = new HashMap<>();

    private Room north;
    private Room east;
    private Room south;
    private Room west;

    public Room(String name,String description){
        this.name=name;
        this.description=description;
    }
    public Room(String name,String description,Room north,Room east,Room south,Room west){
        this.name=name;
        this.description=description;
        this.north=north;
        this.east=east;
        this.south=south;
        this.west=west;
    }

    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return "\n"+this.description;
    }

    public Room getNorth() {
        return north;
    }

    public Room getEast() {
        return east;
    }

    public Room getSouth() {
        return south;
    }

    public Room getWest() {
        return west;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    private String conditionType;
    private String conditionItem;
    private String conditionFailMessage;

    public String getConditionType() { return conditionType; }
    public String getConditionItem() { return conditionItem; }
    public String getConditionFailMessage() { return conditionFailMessage; }

    public void setCondition(String type, String item, String failMessage) {
        this.conditionType = type;
        this.conditionItem = item;
        this.conditionFailMessage = failMessage;
    }

    public boolean hasCondition() { return conditionType != null; }

    public List<Item> getStorage() { return storage; }

    public void setStorage(List<Item> storage) { this.storage = storage; }
    //LOCK
    public void unlock(String direction) {
        Room target = lockedConnections.remove(direction);
        if (target != null) {
            switch (direction) {
                case "north" -> setNorth(target);
                case "south" -> setSouth(target);
                case "east"  -> setEast(target);
                case "west"  -> setWest(target);
            }
        }
    }

    public void lockDirection(String direction, Room target) {
        lockedConnections.put(direction, target);
    }

    public boolean isLocked(String direction) {
        return lockedConnections.containsKey(direction);
    }

    private List<NPC> npcs = new ArrayList<>();
    //DARKNESS
    private String darkDescription;

    public String getDarkDescription() { return darkDescription; }
    public void setDarkDescription(String darkDescription) { this.darkDescription = darkDescription; }

    private boolean lit = false;
    public boolean isLit() { return lit; }
    public void setLit(boolean lit) { this.lit = lit; }
    //NPCS
    public List<NPC> getNpcs() { return npcs; }
    public void setNpcs(List<NPC> npcs) { this.npcs = npcs; }

    public NPC getNpcByName(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(name)) return npc;
        }
        return null;
    }
}

