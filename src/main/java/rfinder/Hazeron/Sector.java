package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class Sector {
    private String name;
    private int x, y, z;
    private HashMap<String, System> systems;
    private Galaxy parent;
    private boolean placeholder;

    public Sector() {
        this.placeholder = true;
        this.name = "Any";
    }

    public Sector(String name, int x, int y, int z, Galaxy parent) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.placeholder = false;
        this.systems = new HashMap<>();
    }

    @Override
    public int hashCode() {
        return (x << 20) | (y << 10) | z;
    }

    public void addSystem(System system, String systemID) {
        systems.put(systemID, system);
    }

    public String getName() {
        return name.equals("Sector") ? String.format("Sector (%d, %d, %d)", x, y, z) : name;
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

    Galaxy getParent() {
        return parent;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }
}
