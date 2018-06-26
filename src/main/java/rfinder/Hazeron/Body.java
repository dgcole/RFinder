package rfinder.Hazeron;

public class Body {
    private final String name;
    private final String zone;
    private final BodyType bodyType;
    private String diameter;
    private final System parent;

    public Body(String name, String zone, BodyType bodyType, System parent) {
        this.name = name;
        this.zone = zone;
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

    BodyType getBodyType() {
        return bodyType;
    }
}
