package rfinder.Hazeron;

public class Zone {
    private Galaxy galaxy;
    private Sector sector;
    private System system;
    private String body, orbitalZone, bodyType;
    private int zone;
    private boolean showZone;
    private int[] qualities;
    private int[] abundances;
    private Object parent;
    private boolean parentIsStar;

    public Zone(int zone, boolean showZone, Star parent) {
        this.system = parent.getParent();
        this.sector = system.getParent();
        this.galaxy = sector.getParent();
        this.body = parent.getName();
        this.orbitalZone = "Star";
        this.bodyType = "Star";
        this.zone = zone;
        this.showZone = showZone;
        this.qualities = new int[ResourceType.values().length];
        for (int i = 0; i < qualities.length; i++) {
            qualities[i] = 0;
        }
        this.abundances = new int[ResourceType.values().length];
        for (int i = 0; i < abundances.length; i++) {
            abundances[i] = 0;
        }
        this.parent = parent;
        this.parentIsStar = true;
    }

    public Zone(int zone, boolean showZone, Planet parent) {
        this.system = parent.getParent();
        this.sector = system.getParent();
        this.galaxy = sector.getParent();
        this.body = parent.getName();
        this.orbitalZone = parent.getZone();
        this.bodyType = parent.getBodyType();
        this.zone = zone;
        this.showZone = showZone;
        this.qualities = new int[ResourceType.values().length];
        for (int i = 0; i < qualities.length; i++) {
            qualities[i] = 0;
        }
        this.abundances = new int[ResourceType.values().length];
        for (int i = 0; i < abundances.length; i++) {
            abundances[i] = 0;
        }
        this.parent = parent;
        this.parentIsStar = false;
    }

    public void setQuality(int index, int quality) {
        qualities[index] = quality;
    }

    public void setAbundance(int index, int abundance) {
        abundances[index] = abundance;
    }

    public String getGalaxyName() {
        return galaxy.getName();
    }

    public String getSectorName() {
        return sector.getName();
    }

    public String getSystemName() {
        return system.getName();
    }

    public String getBodyName() {
        return body;
    }

    public String getOrbitalZone() {
        return orbitalZone;
    }

    public String getBodyType() {
        return bodyType;
    }

    public int getZone() {
        if (showZone) return zone;
        else return 0;
    }

    public int getQuality(int index) {
        return qualities[index];
    }

    public int getAbundance(int index) {
        return abundances[index];
    }
}
