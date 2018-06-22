package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class System {
    private String systemID, name, eod;
    private double x, y, z;
    private HashMap<String, Star> stars;
    private HashMap<String, Planet> planets;
    private Sector parent;
    private boolean placeholder;

    public System() {
        this.placeholder = true;
        this.name = "Any";
    }

    public System(String systemID, String name, String eod, double x, double y, double z, Sector parent) {
        this.systemID = systemID;
        this.name = name;
        this.eod = eod;
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.placeholder = false;
        this.stars = new HashMap<>();
        this.planets = new HashMap<>();
    }

    public void addStar(Star star, String starID) {
        stars.put(starID, star);
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

    public ArrayList<Star> getStars() {
        return new ArrayList<>(stars.values());
    }

    public ArrayList<Planet> getPlanets() {
        return new ArrayList<>(planets.values());
    }

    public Sector getParent() {
        return parent;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }
}
