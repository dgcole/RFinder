package rfinder.Hazeron;

public class System {
    private String name;
    private double x, y, z;
    private Sector parent;
    private boolean placeholder;

    public System() {
        this.placeholder = true;
        this.name = "Any";
    }

    public System(String name, double x, double y, double z, Sector parent) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.parent = parent;
        this.placeholder = false;
    }

    public String getName() {
        return name;
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

    Sector getParent() {
        return parent;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }
}
