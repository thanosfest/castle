package engine.commands;

import engine.model.Player;
import engine.util.CommandParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LoadCommand implements Command {
    private final CommandParser parser;

    public LoadCommand(CommandParser parser) {
        this.parser = parser;
    }

    @Override
    public void execute(Player player, String filePath) {
        if (filePath.isEmpty()) {
            System.out.println("Usage: load <absolute path>  e.g. load C:\\saves\\game.txt");
            return;
        }

        List<String> commands = new ArrayList<>();  
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) commands.add(line.trim());
            }
        } catch (Exception e) {
            System.out.println("Could not load from " + filePath + ": " + e.getMessage());
            return;
        }

        System.out.println("Loading " + commands.size() + " commands...");
        for (String command : commands) {
            System.out.println("> " + command);
            parser.executeReplay(command);
        }
        System.out.println("Load complete.");
    }
}