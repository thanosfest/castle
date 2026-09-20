package engine.util;

import engine.commands.*;
import engine.model.Player;
import engine.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import engine.util.CommandRegistry;

public class CommandParser {
    private final Player player;
    private final Map<String, Room> roomMap;
    private final Scanner scanner;
    private final List<String> history = new ArrayList<>();
    private final CommandRegistry registry;

    public CommandParser(WorldData data) throws Exception {
        this.player   = data.player;
        this.roomMap  = data.rooms;
        this.scanner  = new Scanner(System.in);
        this.registry = new CommandRegistry("src/commands.json");
    }

    public void run() {
        new LookCommand().execute(player, "");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.toLowerCase().split(" ", 2);
            String command  = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            String resolved = registry.resolve(command);

            if (resolved == null) {
                System.out.println("Unknown command: " + command);
                continue;
            }

            switch (resolved) {

                case "go"        -> { new GoCommand().execute(player, argument);         history.add(input); }
                case "look"      -> { new LookCommand().execute(player, argument);       history.add(input); }
                case "get"       -> { new GetCommand().execute(player, argument);        history.add(input); }
                case "use"       -> { new UseCommand(roomMap).execute(player, argument); history.add(input); }
                case "speak"     -> { new SpeakCommand().execute(player, argument);      history.add(input); }
                case "inventory" -> { new InventoryCommand().execute(player, argument); }
                case "save"      -> new SaveCommand(history).execute(player, argument);
                case "load"      -> new LoadCommand(this).execute(player, argument);
                case "quit"      -> { System.out.println("Game has ended!"); scanner.close(); return; }
            }
        }
    }



    public void executeReplay(String input) {
        String[] parts = input.toLowerCase().trim().split(" ", 2);
        String command  = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        String resolved = registry.resolve(command);
        if (resolved == null) {
            System.out.println("Skipping unknown command: " + command);
            return;
        }

        switch (resolved) {
            case "go"        -> new GoCommand().execute(player, argument);
            case "look"      -> new LookCommand().execute(player, argument);
            case "get"       -> new GetCommand().execute(player, argument);
            case "use"       -> new UseCommand(roomMap).execute(player, argument);
            case "speak"     -> new SpeakCommand().execute(player, argument);
        }
    }
}