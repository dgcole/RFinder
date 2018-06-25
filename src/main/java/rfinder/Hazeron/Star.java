package rfinder.Hazeron;

public class Star {
    private final String name, diameter;
    private final System parent;

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
