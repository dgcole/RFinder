package rfinder.Util;

import javafx.scene.paint.Color;

public class Colorizer {
    public static Color getQualityColor(Integer quality) {
        if (quality < 75) return Color.RED;
        else if (quality < 155) return Color.ORANGE;
        else if (quality < 225) return Color.GREEN;
        else if (quality < 245) return Color.DARKGREEN;
        return Color.BLUE;
    }

    public static Color getZoneColor(String zone) {
        if (zone.contains("Star")) return Color.RED;
        else if (zone.contains("Inf")) return Color.RED;
        else if (zone.contains("Inn")) return Color.ORANGE;
        else if (zone.contains("Hab")) return Color.GREEN;
        else if (zone.contains("Out")) return Color.BLUE;
        else if (zone.contains("Fri")) return Color.LIGHTBLUE;
        else return Color.BLACK;
    }
}
