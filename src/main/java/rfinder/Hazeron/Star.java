package rfinder.Hazeron;

public class Star {
    private String name, diameter;
    private System parent;

    public Star(String name, String diameter, System parent) {
        this.name = name;
        this.diameter = diameter;
        this.parent = parent;
    }

    String getName() {
        return name;
    }

    String getDiameter() {
        return diameter;
    }

    System getParent() {
        return parent;
    }
}
