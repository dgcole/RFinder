package rfinder.Tasks;

import javafx.concurrent.Task;
import rfinder.Controller.MainController;
import rfinder.Hazeron.System;
import rfinder.Hazeron.*;

import java.util.ArrayList;

public class ResourceFilterTask extends Task<ArrayList<Resource>> {
    private final ArrayList<Resource> resources;
    private final ResourceType type;
    private final String range, diameter, zone;
    private final int minQual;
    private final Galaxy galaxy;
    private final Sector sector;
    private final System system;

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
    protected ArrayList<Resource> call() {
        ArrayList<Resource> matches = new ArrayList<>();

        for (Resource r : resources) {
            System rSystem = r.getSystemInternal();

            boolean resourceMatch = (type == null || type == ResourceType.ANY) || r.getResourceType() == type;

            boolean qualityMatch = (r.getQ1() >= minQual ||
                    r.getQ2() >= minQual || r.getQ3() >= minQual);

            boolean galaxyMatch = (galaxy == null || galaxy.isPlaceholder())
                    || galaxy == r.getGalaxyInternal();

            boolean sectorMatch = (sector == null || sector.isPlaceholder())
                    || sector == r.getSectorInternal();

            boolean systemMatch = (system == null || system.isPlaceholder())
                    || system == rSystem;

            boolean diameterMatch = (diameter == null || diameter.equals("Any"))
                    || (diameter.equals("Ringworld") && r.getBody().contains("Ringworld"));

            boolean zoneMatch = (zone == null || zone.equals("Any"))
                    || zone.equals(r.getZone());

            if (!diameterMatch) {
                diameterMatch = diameter.equals(r.getDiameter());
            }

            int parsecs = range.isEmpty() ? 0 : Integer.parseInt(range);
            boolean rangeMatch = MainController.checkRange(sector, system, rSystem, parsecs);

            if (resourceMatch && qualityMatch && diameterMatch && zoneMatch && ((galaxyMatch && sectorMatch && systemMatch) || rangeMatch)) {
                matches.add(r);
            }
        }
        return matches;
    }
}
