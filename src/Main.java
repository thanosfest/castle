import engine.util.WorldData;
import engine.util.CommandParser;
import engine.util.WorldLoader;


void main() {
    try {
        WorldData data = WorldLoader.loadFromJson("src/engine/util/worlds/world.json");
        new CommandParser(data).run();
    } catch (Exception e) {
        e.printStackTrace();
    }
}