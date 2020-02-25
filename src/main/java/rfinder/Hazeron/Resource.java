package rfinder.Hazeron;

import javafx.fxml.FXML;

public class Resource {
    private final ResourceType type;
    private final int q1, q2, q3, q4, a1, a2, a3, a4;
    private final Object parent;
    private final boolean parentIsStar;

    public Resource(ResourceType type, int q1, int q2, int q3, int q4, int a1, int a2, int a3, int a4, Star parent) {
        this.type = type;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = q4;
        this.parent = parent;
        this.parentIsStar = true;
    }

    public Resource(ResourceType type, int q1, int q2, int q3, int q4, int a1, int a2, int a3, int a4, Body parent) {
        this.type = type;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = q4;
        this.parent = parent;
        this.parentIsStar = false;
    }

    @FXML
    public ResourceType getResourceType() {
        return type;
    }

    @FXML
    public String getGalaxy() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getParent().getName();
        } else {
            return ((Body) parent).getParent().getParent().getParent().getName();
        }
    }

    public Galaxy getGalaxyInternal() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getParent();
        } else {
            return ((Body) parent).getParent().getParent().getParent();
        }
    }

    @FXML
    public String getSector() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent().getName();
        } else {
            return ((Body) parent).getParent().getParent().getName();
        }
    }

    public Sector getSectorInternal() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getParent();
        } else {
            return ((Body) parent).getParent().getParent();
        }
    }

    @FXML
    public String getSystem() {
        if (parentIsStar) {
            return ((Star) parent).getParent().getName();
        } else {
            return ((Body) parent).getParent().getName();
        }
    }

    public System getSystemInternal() {
        if (parentIsStar) {
            return ((Star) parent).getParent();
        } else {
            return ((Body) parent).getParent();
        }
    }

    @FXML
    public String getBody() {
        if (parentIsStar) {
            return ((Star) parent).getName();
        } else {
            return ((Body) parent).getName();
        }
    }

    @FXML
    public String getDiameter() {
        if (parentIsStar) {
            return "N/A";
        } else {
            return ((Body) parent).getDiameter();
        }
    }

    @FXML
    public String getZone() {
        if (parentIsStar) {
            return "Star";
        } else {
            return ((Body) parent).getZone();
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
    public int getQ4() {
        return q4;
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

    @FXML
    public int getA4() {
        return a4;
    }
}
