package rfinder.Hazeron;

public class Body {
    private final String name, zone, orbit;
    private final BodyType bodyType;
    private String diameter;
    private final System parent;

    public Body(String name, String zone, String orbit, BodyType bodyType, System parent) {
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

    public BodyType getBodyType() {
        return bodyType;
    }
}
