package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class StarMap {
    private final HashMap<String, Galaxy> galaxies;
    private final ArrayList<Zone> zones;
    private final HashMap<String, System> systems;

    public StarMap() {
        galaxies = new HashMap<>();
        zones = new ArrayList<>();
        systems = new HashMap<>();
    }

    public void addGalaxy(Galaxy galaxy, String galaxyName) {
        galaxies.put(galaxyName, galaxy);
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public void addSystem(System system, String systemID) {
        systems.put(systemID, system);
    }

    public ArrayList<Galaxy> getGalaxies() {
        return new ArrayList<>(galaxies.values());
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public ArrayList<System> getSystems() {
        return new ArrayList<>(systems.values());
    }
}
