package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.HashMap;

public class Galaxy {
    private final String name;
    private HashMap<String, Sector> sectors;
    private final boolean placeholder;

    public Galaxy() {
        this.placeholder = true;
        this.name = "Any";
    }
    public Galaxy(String name) {
        this.name = name;
        this.placeholder = false;
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

    public boolean isPlaceholder() {
        return placeholder;
    }
}
