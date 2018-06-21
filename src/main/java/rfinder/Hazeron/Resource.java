package rfinder.Hazeron;

import javafx.fxml.FXML;

public class Resource {
    private ResourceType type;
    private int zones, q1, q2, q3, a1, a2, a3;
    private String sphere;
    private Object parent;
    private boolean parentIsStar;

    public Resource(ResourceType type, int zones, int q1, int q2, int q3, int a1, int a2, int a3, String sphere, Star parent) {
        this.type = type;
        this.zones = zones;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.sphere = sphere;
        this.parent = parent;
        this.parentIsStar = true;
    }

    public Resource(ResourceType type, int zones, int q1, int q2, int q3, int a1, int a2, int a3, String sphere, Planet parent) {
        this.type = type;
        this.zones = zones;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.sphere = sphere;
        this.parent = parent;
        this.parentIsStar = false;
    }

    @FXML
    public String getResource() {
        return type.toString();
    }

    @FXML
    public String getGalaxy() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getParent().getName();
        } else {
            return ((Planet) parent).getParent().getParent().getParent().getName();
        }
    }

    public Galaxy getGalaxyInternal() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getParent();
        } else {
            return ((Planet) parent).getParent().getParent().getParent();
        }
    }

    @FXML
    public String getSector() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getName();
        } else {
            return ((Planet) parent).getParent().getParent().getName();
        }
    }

    public Sector getSectorInternal() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent();
        } else {
            return ((Planet) parent).getParent().getParent();
        }
    }

    @FXML
    public String getSystem() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getName();
        } else {
            return ((Planet) parent).getParent().getName();
        }
    }

    public System getSystemInternal() {
        if (parentIsStar) {
            return ((Star) parent).getParent();
        } else {
            return ((Planet) parent).getParent();
        }
    }

    @FXML
    public String getBody() {
        if (parentIsStar) {
            return ((Star) parent).getName();
        } else {
            return ((Planet) parent).getName();
        }
    }

    @FXML
    public String getDiameter() {
        if (parentIsStar) {
            return ((Star) parent).getDiameter();
        } else {
            return ((Planet) parent).getDiameter();
        }
    }

    @FXML
    public int getQ1() {
        return q1;
    }

    @FXML
    public int getQ2() {
        return q2;
    }

    @FXML
    public int getQ3() {
        return q3;
    }

    @FXML
    public int getA1() {
        return a1;
    }

    @FXML
    public int getA2() {
        return a2;
    }

    @FXML
    public int getA3() {
        return a3;
    }
}
