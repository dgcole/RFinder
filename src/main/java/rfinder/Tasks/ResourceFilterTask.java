package rfinder.Tasks;

import javafx.concurrent.Task;
import rfinder.Hazeron.Galaxy;
import rfinder.Hazeron.Resource;
import rfinder.Hazeron.Sector;
import rfinder.Hazeron.System;

import java.util.ArrayList;

public class ResourceFilterTask extends Task<ArrayList<Resource>> {
    private ArrayList<Resource> resources;
    private String type, range, diameter, zone;
    private int minQual;
    private Galaxy galaxy;
    private Sector sector;
    private System system;

    public ResourceFilterTask(ArrayList<Resource> resources, String type, int minQual, Galaxy galaxy, Sector sector, System system, String range, String diameter, String zone) {
        this.resources = resources;
        this.type = type;
        this.minQual = minQual;
        this.galaxy = galaxy;
        this.sector = sector;
        this.system = system;
        this.range = range;
        this.diameter = diameter;
        this.zone = zone;
    }

    @Override
    protected ArrayList<Resource> call() throws Exception {
        ArrayList<Resource> matches = new ArrayList<>();

        for (Resource r : resources) {
            boolean resourceMatch = (type == null || type.equals("Any")) || r.getResource().equals(type);

            boolean qualityMatch = (r.getQ1() >= minQual ||
                    r.getQ2() >= minQual || r.getQ3() >= minQual);

            boolean galaxyMatch = (galaxy == null || galaxy.isPlaceholder())
                    || galaxy == r.getGalaxyInternal();

            boolean sectorMatch = (sector == null || sector.isPlaceholder())
                    || sector == r.getSectorInternal();

            boolean systemMatch = (system == null || system.isPlaceholder())
                    || system == r.getSystemInternal();

            boolean diameterMatch = (diameter == null || diameter.equals("Any"))
                    || (diameter.equals("Ringworld") && r.getBody().contains("Ringworld"));

            boolean zoneMatch = (zone == null || zone.equals("Any"))
                    || zone.equals(r.getZone());

            if (!diameterMatch) {
                diameterMatch = diameter.equals(r.getDiameter());
            }

            boolean rangeMatch = false;

            if (!range.isEmpty()) {
                double targetX, targetY, targetZ;
                targetX = targetY = targetZ = 0;
                boolean set = false;
                if ((system == null || system.isPlaceholder())
                        && (sector != null && !sector.isPlaceholder())) {
                    Sector target = sector;
                    targetX = target.getX() * 10;
                    targetY = target.getY() * 10;
                    targetZ = target.getZ() * 10;
                    set = true;
                } else if ((system != null && !system.isPlaceholder())) {
                    System target = system;
                    targetX = target.getX();
                    targetY = target.getY();
                    targetZ = target.getZ();
                    set = true;
                }

                if (set) {
                    double originX = r.getSystemInternal().getX();
                    double originY = r.getSystemInternal().getY();
                    double originZ = r.getSystemInternal().getZ();

                    double dist = Math.sqrt((targetX - originX) * (targetX - originX) +
                            (targetY - originY) * (targetY - originY) + (targetZ - originZ) * (targetZ - originZ));
                    if (dist < Double.parseDouble(range)) rangeMatch = true;
                } else {
                    rangeMatch = true;
                }
            }


            if (resourceMatch && qualityMatch && diameterMatch && zoneMatch && ((galaxyMatch && sectorMatch && systemMatch) || rangeMatch)) {
                matches.add(r);
            }
        }
        return matches;
    }
}
