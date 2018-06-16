package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class StarMap {
    private String empireName;
    private HashMap<String, Galaxy> galaxies;

    public StarMap(String empireName) {
        this.empireName = empireName;
        galaxies = new HashMap<>();
    }

    public String getEmpireName() {
        return empireName;
    }

    public void addGalaxy(Galaxy galaxy, String galaxyName) {
        galaxies.put(galaxyName, galaxy);
    }

    public ArrayList<Galaxy> getGalaxies() {
        return new ArrayList<>(galaxies.values());
    }
}
