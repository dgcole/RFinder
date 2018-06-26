package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class StarMap {
    private final HashMap<String, Galaxy> galaxies;
    private final ArrayList<Resource> resources;
    private final ArrayList<Zone> zones;

    public StarMap() {
        galaxies = new HashMap<>();
        resources = new ArrayList<>();
        zones = new ArrayList<>();
    }

    public void addGalaxy(Galaxy galaxy, String galaxyName) {
        galaxies.put(galaxyName, galaxy);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public ArrayList<Galaxy> getGalaxies() {
        return new ArrayList<>(galaxies.values());
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }
}
