package engine.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, String> aliasMap = new HashMap<>();

    public CommandRegistry(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new FileInputStream(path));

        for (JsonNode entry : root.get("commands")) {
            String name = entry.get("name").asText();
            aliasMap.put(name, name);

            for (JsonNode alias : entry.get("aliases")) {
                aliasMap.put(alias.asText(), name);
            }
        }
    }

    // returns canonical command name, or null if not found
    public String resolve(String input) {
        return aliasMap.get(input.toLowerCase());
    }
}