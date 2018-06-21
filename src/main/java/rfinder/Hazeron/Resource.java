package rfinder.Hazeron;

import javafx.fxml.FXML;

public class Resource {
    private ResourceType type;
    private int zones;
    private String sphere,  q1, q2, q3, a1, a2, a3;
    private Object parent;
    private boolean parentIsStar;

    public Resource(ResourceType type, int zones, int q1, int q2, int q3, int a1, int a2, int a3, String sphere, Star parent) {
        this.type = type;
        this.zones = zones;
        this.q1 = q1 == 0 ? "N/A" : Integer.toString(q1);
        this.q2 = q2 == 0 ? "N/A" : Integer.toString(q2);
        this.q3 = q3 == 0 ? "N/A" : Integer.toString(q3);
        this.a1 = a1 == 0 ? "N/A" : Integer.toString(a1);
        this.a2 = a2 == 0 ? "N/A" : Integer.toString(a2);
        this.a3 = a3 == 0 ? "N/A" : Integer.toString(a3);
        this.sphere = sphere;
        this.parent = parent;
        this.parentIsStar = true;
    }

    public Resource(ResourceType type, int zones, int q1, int q2, int q3, int a1, int a2, int a3, String sphere, Planet parent) {
        this.type = type;
        this.zones = zones;
        this.q1 = q1 == 0 ? "N/A" : Integer.toString(q1);
        this.q2 = q2 == 0 ? "N/A" : Integer.toString(q2);
        this.q3 = q3 == 0 ? "N/A" : Integer.toString(q3);
        this.a1 = a1 == 0 ? "N/A" : Integer.toString(a1);
        this.a2 = a2 == 0 ? "N/A" : Integer.toString(a2);
        this.a3 = a3 == 0 ? "N/A" : Integer.toString(a3);
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

    @FXML
    public String getSector() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getName();
        } else {
            return ((Planet) parent).getParent().getParent().getName();
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

    @FXML
    public String getBody() {
        if (parentIsStar) {
            return ((Star) parent).getName();
        } else {
            return ((Planet) parent).getName();
        }
    }

    @FXML
    public String getQ1() {
        return q1;
    }

    @FXML
    public String getQ2() {
        return q2;
    }

    @FXML
    public String getQ3() {
        return q2;
    }

    @FXML
    public String getA1() {
        return a1;
    }

    @FXML
    public String getA2() {
        return a2;
    }

    @FXML
    public String getA3() {
        return a3;
    }
}
