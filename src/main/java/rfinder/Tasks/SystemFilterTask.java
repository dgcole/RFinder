package rfinder.Tasks;

import javafx.concurrent.Task;
import rfinder.Filter.ResourceFilter;
import rfinder.Hazeron.ResourceType;
import rfinder.Hazeron.Zone;
import rfinder.Hazeron.System;

import java.util.ArrayList;

public class SystemFilterTask extends Task<ArrayList<System>> {
    private ArrayList<System> systems;
    private ArrayList<ResourceFilter> resourceFilters;

    public SystemFilterTask(ArrayList<System> systems, ArrayList<ResourceFilter> resourceFilters) {
        this.systems = systems;
        this.resourceFilters = resourceFilters;
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

        return matches;
    }
}
