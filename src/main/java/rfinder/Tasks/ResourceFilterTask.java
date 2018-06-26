package rfinder.Tasks;

import javafx.concurrent.Task;
import rfinder.Controller.MainController;
import rfinder.Hazeron.*;
import rfinder.Hazeron.System;

import java.util.ArrayList;

public class ResourceFilterTask extends Task<ArrayList<Resource>> {
    private ArrayList<Resource> resources;
    private ResourceType type;
    private String range, diameter, zone;
    private int minQual;
    private Galaxy galaxy;
    private Sector sector;
    private System system;

    public ResourceFilterTask(ArrayList<Resource> resources, ResourceType type, int minQual, Galaxy galaxy, Sector sector, System system, String range, String diameter, String zone) {
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
            boolean resourceMatch = (type == null || type == ResourceType.ANY) || r.getResourceType() == type;

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

            int parsecs = range.isEmpty() ? 0 : Integer.parseInt(range);
            boolean rangeMatch = MainController.checkRange(sector, system, r.getSystemInternal(), parsecs);

            if (resourceMatch && qualityMatch && diameterMatch && zoneMatch && ((galaxyMatch && sectorMatch && systemMatch) || rangeMatch)) {
                matches.add(r);
            }
        }
        return matches;
    }
}
