package engine.commands;

import engine.model.Player;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class SaveCommand implements Command {
    private final List<String> history;

    public SaveCommand(List<String> history) {
        this.history = history;
    }

    @Override
    public void execute(Player player, String fileName) {
        if (fileName.isEmpty()) {
            System.out.println("Usage: save <filename>  e.g. save mysave");
            return;
        }

        // strip .txt if they typed it, then add it back
        if (fileName.endsWith(".txt")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }

        String filePath = "saves/" + fileName + ".txt";

        // create saves folder if it doesn't exist
        new java.io.File("saves").mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String command : history) {
                writer.println(command);
            }
            System.out.println("Game saved to " + filePath);
        } catch (Exception e) {
            System.out.println("Could not save: " + e.getMessage());
        }
    }
}