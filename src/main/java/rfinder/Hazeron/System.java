package rfinder.Hazeron;

import java.util.ArrayList;

public class System {
    private final String name, id;
    private double x, y, z;
    private Sector parent;
    private final boolean placeholder;
    private ArrayList<Zone> zones;
    private ArrayList<Wormhole> wormholes;

    public System() {
        this.placeholder = true;
        this.name = "Any";
        this.id = "";
    }

    public System(String name, String id, double x, double y, double z, Sector parent) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.placeholder = false;
        this.zones = new ArrayList<>();
        this.wormholes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Sector getParent() {
        return parent;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public void addWormhole(Wormhole wormhole) {
        wormholes.add(wormhole);
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public ArrayList<Wormhole> getWormholes() {
        return wormholes;
    }
}
