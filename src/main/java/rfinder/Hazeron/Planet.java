package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class Planet {
    private String planetID, name, bodyType, orbit, zone, diameter;
    private HashMap<ResourceType, Resource> resources;
    private System parent;

    public Planet(String planetID, String name, String bodyType, String orbit, String zone, System parent) {
        this.planetID = planetID;
        this.name = name;
        this.bodyType = bodyType;
        this.orbit = orbit;
        this.zone = zone;
        this.parent = parent;
        this.resources = new HashMap<>();
    }

    public void addResource(Resource resource, ResourceType type) {
        resources.put(type, resource);
    }

    public String getID() {
        return planetID;
    }

    public String getName() {
        return name;
    }

    public String getBodyType() {
        return bodyType;
    }

    public String getOrbit() {
        return orbit;
    }

    public String getZone() {
        return zone;
    }

    public ArrayList<Resource> getResources() {
        return new ArrayList<>(resources.values());
    }

    public System getParent() {
        return parent;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getDiameter() {
        return diameter;
    }
}
