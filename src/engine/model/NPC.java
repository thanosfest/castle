package engine.model;

import java.util.List;

public class NPC {
    private String name;
    private List<String> dialogue;
    private int dialogueIndex = 0;
    private boolean spoken = false;

    public NPC(String name, List<String> dialogue) {
        this.name = name;
        this.dialogue = dialogue;
    }

    public String getName() { return name; }
    public boolean hasSpoken() { return spoken; }

    public String speak(String playerName) {
        if (dialogue.isEmpty()) return "...";
        spoken = true;
        String line = dialogue.get(dialogueIndex);
        line = line.replace("{playerName}", playerName);
        dialogueIndex = (dialogueIndex + 1) % dialogue.size();
        return line;
    }
}