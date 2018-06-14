package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class Sector {
    private String id, name;
    private int x, y, z;
    private HashMap<String, System> systems;

    public Sector(String id, String name, int x, int y, int z) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int hashCode() {
        return (x << 20) | (y << 10) | z;
    }

    public void addSystem(System system, String systemID) {
        systems.put(systemID, system);
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public ArrayList<System> getSystems() {
        return new ArrayList<>(systems.values());
    }
}
