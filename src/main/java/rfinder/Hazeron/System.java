package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class System {
    private String systemID, name, eod;
    private double x, y, z;
    private Star star;
    private HashMap<String, Planet> planets;

    public System(String systemID, String name, String eod, double x, double y, double z) {
        this.systemID = systemID;
        this.name = name;
        this.eod = eod;
        this.x = x;
        this.y = y;
        this.z = z;
        this.star = null;
        this.planets = new HashMap<>();
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public void addPlanet(Planet planet, String planetID) {
        planets.put(planetID, planet);
    }

    public String getID() {
        return systemID;
    }

    public String getName() {
        return name;
    }

    public String getEOD() {
        return eod;
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

    public Star getStar() {
        return star;
    }

    public ArrayList<Planet> getPlanets() {
        return new ArrayList<>(planets.values());
    }
}
