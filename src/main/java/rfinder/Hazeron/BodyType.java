package rfinder.Hazeron;

public enum BodyType {
    GAS_GIANT("Gas Giant"), PLANET("Planet"),
    LARGE_MOON("Large Moon"), MOON("Moon"),
    RINGWORLD("Ringworld"), RING("Ring"),
    STAR("Star");

    private final String text;

    BodyType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static BodyType getType(String type) {
        if (type.contains("Ringworld")) return RINGWORLD;
      for (BodyType bt : values()) {
          if (type.equals(bt.text)) return bt;
      }
      return PLANET;
    }
}
