package rfinder.Util;

import javafx.scene.paint.Color;

public class QualityColorizer {
    public static Color getColor(Integer quality) {
        if (quality < 75) return Color.RED;
        else if (quality < 155) return Color.ORANGE;
        else if (quality < 225) return Color.GREEN;
        else if (quality < 245) return Color.DARKGREEN;
        return Color.BLUE;
    }
}
