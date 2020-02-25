package rfinder.Hazeron;

import java.util.HashMap;

public class Zone {
    private final Galaxy galaxy;
    private final Sector sector;
    private final System system;
    private final String body, orbitalZone;
    private final BodyType bodyType;
    private final int zone;
    private final boolean showZone;
    private final int[] qualities, abundances;

    public Zone(int zone, boolean showZone, Star parent) {
        this.system = parent.getParent();
        this.sector = system.getParent();
        this.galaxy = sector.getParent();
        this.body = parent.getName();
        this.orbitalZone = "";
        this.bodyType = BodyType.STAR;
        this.zone = zone;
        this.showZone = showZone;
        this.qualities = new int[ResourceType.getTypes().size()];
        for (int i = 0; i < qualities.length; i++) {
            qualities[i] = 0;
        }
        this.abundances = new int[ResourceType.getTypes().size()];
        for (int i = 0; i < abundances.length; i++) {
            abundances[i] = 0;
        }
    }

    public Zone(int zone, boolean showZone, Body parent) {
        this.system = parent.getParent();
        this.sector = system.getParent();
        this.galaxy = sector.getParent();
        this.body = parent.getName();
        this.orbitalZone = parent.getZone();
        this.bodyType = parent.getBodyType();
        this.zone = zone;
        this.showZone = showZone;
        this.qualities = new int[ResourceType.getTypes().size()];
        for (int i = 0; i < qualities.length; i++) {
            qualities[i] = 0;
        }
        this.abundances = new int[ResourceType.getTypes().size()];
        for (int i = 0; i < abundances.length; i++) {
            abundances[i] = 0;
        }
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

    public BodyType getBodyType() {
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

    public Galaxy getGalaxy() {
        return galaxy;
    }

    public Sector getSector() {
        return sector;
    }

    public System getSystem() {
        return system;
    }

    public boolean hasQuality(int threshold) {
        for (int q : qualities) {
            if (q >= threshold) return true;
        }
        return false;
    }
}
