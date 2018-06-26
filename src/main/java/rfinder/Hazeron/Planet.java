package rfinder.Hazeron;

public class Planet {
    private final String name, zone, orbit, bodyType;
    private String diameter;
    private final System parent;

    public Planet(String name, String zone, String orbit, String bodyType, System parent) {
        this.name = name;
        this.zone = zone;
        this.orbit = orbit;
        this.bodyType = bodyType;
        this.parent = parent;
    }

    String getName() {
        return name;
    }

    String getZone() {
        return zone;
    }

    System getParent() {
        return parent;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    String getDiameter() {
        return diameter;
    }

    public String getOrbit() {
        return orbit;
    }

    public String getBodyType() {
        return bodyType;
    }
}
