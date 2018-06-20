package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class StarMap {
    private String empireName;
    private HashMap<String, Galaxy> galaxies;
    private ArrayList<Resource> resources;

    public StarMap(String empireName) {
        this.empireName = empireName;
        galaxies = new HashMap<>();
        resources = new ArrayList<>();
    }

    public String getEmpireName() {
        return empireName;
    }

    public void addGalaxy(Galaxy galaxy, String galaxyName) {
        galaxies.put(galaxyName, galaxy);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public ArrayList<Galaxy> getGalaxies() {
        return new ArrayList<>(galaxies.values());
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }
}
