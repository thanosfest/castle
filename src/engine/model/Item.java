package engine.model;

public class Item {
    private String name;
    private String description;
    private String effect;
    private String message;

    public Item(String name, String description, String effect, String message) {
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.message = message;
    }

    public String getName() { return name; }
    public String getDescription() { return "\n" + description; }
    public String getEffect() { return effect; }
    public String getMessage() { return message; }
}