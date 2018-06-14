package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class Star {
    private String starID, name, orbit, spectralClass, size, hab, shell, diameter;
    private HashMap<ResourceType, Resource> resources;

    public Star(String starID, String name, String orbit, String spectralClass, String size, String hab, String shell, String diameter) {
        this.starID = starID;
        this.name = name;
        this.orbit = orbit;
        this.spectralClass = spectralClass;
        this.size = size;
        this.hab = hab;
        this.shell = shell;
        this.diameter = diameter;
        this.resources = new HashMap<>();
    }

    public void addResource(Resource resource, ResourceType type) {
        resources.put(type, resource);
    }

    public String getID() {
        return starID;
    }

    public String getName() {
        return name;
    }

    public String getOrbit() {
        return orbit;
    }

    public String getSpectralClass() {
        return spectralClass;
    }

    public String getSize() {
        return size;
    }

    public String getHab() {
        return hab;
    }

    public String getShell() {
        return shell;
    }

    public String getDiameter() {
        return diameter;
    }

    public ArrayList<Resource> getResources() {
        return new ArrayList<>(resources.values());
    }
}
