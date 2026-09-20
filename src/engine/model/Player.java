package engine.model;

import java.util.List;

public class Player {
    private List<Item> inventory;
    private Room currentRoom;
    private String name;

    public Player(List<Item> inventory, Room currentRoom) {
        this.inventory = inventory;
        this.currentRoom = currentRoom;
    }
    public Player(List<Item> inventory, Room currentRoom, String name) {
        this.inventory = inventory;
        this.currentRoom = currentRoom;
        this.name=name;
    }
    public void setRoom(Room nextRoom){
        this.currentRoom=nextRoom;
    }
    public Room getRoom(){
        return this.currentRoom;
    }


    public String getName(){
        return this.name;
    }


    public List<Item> getInventory() { return inventory; }

    public void addItem(Item item) { inventory.add(item); }
}

