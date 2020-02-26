package rfinder.Tasks;

import javafx.concurrent.Task;
import rfinder.Controller.MainController;
import rfinder.Filter.RangeFilter;
import rfinder.Filter.ResourceFilter;
import rfinder.Hazeron.ResourceType;
import rfinder.Hazeron.System;
import rfinder.Hazeron.Zone;

import java.util.ArrayList;

public class SystemFilterTask extends Task<ArrayList<System>> {
    private ArrayList<System> systems;
    private ArrayList<ResourceFilter> resourceFilters;
    private ArrayList<RangeFilter> rangeFilters;

    public SystemFilterTask(ArrayList<System> systems, ArrayList<ResourceFilter> resourceFilters,
                            ArrayList<RangeFilter> rangeFilters) {
        this.systems = systems;
        this.resourceFilters = resourceFilters;
        this.rangeFilters = rangeFilters;
    }

    @Override
    protected ArrayList<System> call() {
        ArrayList<System> matches = new ArrayList<>(systems);

        for (ResourceFilter filter : resourceFilters) {
            matches.removeIf(system -> {
                ResourceType type = filter.getType();
                int typeIndex = ResourceType.indexOf(type);
                int quality = filter.getQuality();
                boolean found = false;
                for (Zone zone : system.getZones()) {
                    if ((type == ResourceType.ANY && zone.hasQuality(quality)) || (zone.getQuality(typeIndex) != 0 && zone.getQuality(typeIndex) >= quality)) {
                        found = true;
                    }
                }
                return !found;
            });
        }
        for (RangeFilter filter : rangeFilters) {
            matches.removeIf(system -> {
                boolean galaxyMatch = filter.getGalaxy().isPlaceholder()
                        || filter.getGalaxy() == system.getParent().getParent();

                boolean sectorMatch = filter.getSector().isPlaceholder()
                        || filter.getSector() == system.getParent();

                boolean systemMatch = filter.getSystem().isPlaceholder() || filter.getSystem() == system;

                boolean rangeMatch = MainController.checkRange(filter.getSector(), filter.getSystem(), system,
                        filter.getRange());

                return !((galaxyMatch && sectorMatch && systemMatch) || rangeMatch);
            });
        }

        return matches;
    }
}
