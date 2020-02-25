package rfinder.Hazeron;





import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public enum BodyType {
    GAS_GIANT("Gas Giant"), PLANET("Planet"),
    MOON("Moon"), RINGWORLD("Ringworld"),
    RING("Ring"), STAR("Star"),
    TITAN("Titan"), ASTEROID("Asteroid");

    private final String text;
    private static final ArrayList<BodyType> sortedValues = new ArrayList<>(Arrays.asList(values()));
    static {
        sortedValues.sort(Comparator.comparing(BodyType::toString));
    }

    BodyType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static BodyType getType(String type) {
        if (type.contains("Ringworld")) return RINGWORLD;

        for (BodyType bt : sortedValues) {
            if (type.equals(bt.text)) return bt;
        }

        return PLANET;
    }
}
