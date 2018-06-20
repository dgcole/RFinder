package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class Galaxy {
    private String name;
    private HashMap<String, Sector> sectors;
    private StarMap parent;

    public Galaxy(String name, StarMap parent) {
        this.name = name;
        this.parent = parent;
        sectors = new HashMap<>();
    }

    public void addSector(Sector sector, String sectorID) {
        sectors.put(sectorID, sector);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Sector> getSectors() {
        return new ArrayList<>(sectors.values());
    }

    public StarMap getParent() {
        return parent;
    }
}
