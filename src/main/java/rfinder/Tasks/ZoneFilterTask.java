package rfinder.Tasks;

import javafx.concurrent.Task;
import rfinder.Controller.Main;
import rfinder.Hazeron.Galaxy;
import rfinder.Hazeron.Sector;
import rfinder.Hazeron.Zone;
import rfinder.Hazeron.System;

import java.util.ArrayList;

public class ZoneFilterTask extends Task<ArrayList<Zone>> {
    private ArrayList<Zone> zones;
    private Galaxy galaxy;
    private Sector sector;
    private System system;
    private String range;

    public ZoneFilterTask(ArrayList<Zone> zones, Galaxy galaxy, Sector sector, System system, String range) {
        this.zones = zones;
        this.galaxy = galaxy;
        this.sector = sector;
        this.system = system;
        this.range = range;
    }

    @Override
    protected ArrayList<Zone> call() throws Exception {
        ArrayList<Zone> matches = new ArrayList<Zone>();

        for (Zone z : zones) {
            boolean galaxyMatch = (galaxy == null || galaxy.isPlaceholder()) || galaxy == z.getGalaxy();

            boolean sectorMatch = (sector == null || sector.isPlaceholder()) || sector == z.getSector();

            boolean systemMatch = (system == null || system.isPlaceholder()) || system == z.getSystem();

            int parsecs = range.isEmpty() ? 0 : Integer.parseInt(range);
            boolean rangeMatch = Main.checkRange(sector, system, z.getSystem(), parsecs);

            if ((galaxyMatch && sectorMatch && systemMatch) || rangeMatch) {
                matches.add(z);
            }
        }

        return matches;
    }
}
