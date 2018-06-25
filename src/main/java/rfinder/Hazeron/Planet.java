package rfinder.Hazeron;

public class Planet {
    private String name, zone, diameter;
    private System parent;

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
