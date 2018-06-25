package rfinder.Hazeron;

public class Planet {
    private final String name, zone;
    private String diameter;
    private final System parent;

    public Planet(String name, String zone, System parent) {
        this.name = name;
        this.zone = zone;
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
}
