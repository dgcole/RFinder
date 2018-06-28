package rfinder.Util;

import javafx.scene.paint.Color;

import java.util.HashMap;

public class Colorizer {
    private static final Color[] qualityLookup = new Color[256];
    static {
        for (int i = 0; i < 75; i++) {
            qualityLookup[i] = Color.RED;
        }
        for (int i = 75; i < 155; i++) {
            qualityLookup[i] = Color.ORANGE;
        }
        for (int i = 155; i < 225; i++) {
            qualityLookup[i] = Color.GREEN;
        }
        for (int i = 225; i < 245; i++) {
            qualityLookup[i] = Color.BLUE;
        }
        for (int i = 245; i < 256; i++) {
            qualityLookup[i] = Color.LIGHTBLUE;
        }
    }
    private static final HashMap<String, Color>  zoneLookup = new HashMap<String, Color>() {
        {
            put("Star", Color.RED);
            put("Inferno", Color.RED);
            put("Inner", Color.ORANGE);
            put("Habitable", Color.GREEN);
            put("Outer", Color.BLUE);
            put("Frigid", Color.LIGHTBLUE);
        }
    };

    public static Color getQualityColor(Integer quality) {
        return qualityLookup[quality];
    }

    public static Color getZoneColor(String zone) {
        return zoneLookup.get(zone);
    }
}
