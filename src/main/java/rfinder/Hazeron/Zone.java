package rfinder.Hazeron;

import java.util.HashMap;

public class Zone {
    private final Galaxy galaxy;
    private final Sector sector;
    private final System system;
    private final String body, orbitalZone;
    private final BodyType bodyType;
    private final int zone, pop;
    private final boolean showZone;
    private final int[] qualities, abundances;
    private static final HashMap<String, Integer> popLookup = new HashMap<String, Integer>() {
        {
            put("1900m", 11269);
            put("3800m", 45076);
            put("5700m", 101420);
            put("7600m", 180303);
            put("9500m", 281723);
            put("11400m", 405681);
            put("13300m", 552177);
            put("15200m", 721210);
            put("17000m", 912782);
            put("18900m", 1126891);
            put("20800m", 1363538);
            put("22700m", 1622723);
        }
    };

    public Zone(int zone, boolean showZone, Star parent) {
        this.system = parent.getParent();
        this.sector = system.getParent();
        this.galaxy = sector.getParent();
        this.body = parent.getName();
        this.orbitalZone = "";
        this.bodyType = BodyType.STAR;
        this.zone = zone;
        this.pop = 0;
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
        if (bodyType == BodyType.RING) {
            pop = 0;
        } else if (bodyType == BodyType.RINGWORLD){
            pop = 4102166;
        } else {
            pop = popLookup.get(parent.getDiameter());
        }
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

    public int getPopulationLimit() {
        return pop;
    }
}
