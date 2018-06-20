package rfinder.Hazeron;

import java.util.ArrayList;

public enum ResourceType {

    ANIMAL_CARCASS("Animal Carcass"), BEANS("Beans"),
    CHEESE("Cheese"), EGGS("Eggs"),
    FERTILIZER("Fertilizer"), FISH("Fish"),
    FRUIT("Fruit"), GRAIN("Grain"),
    GRAPES("Grapes"), HERBS("Herbs"),
    HOPS("Hops"), HAY("Hay"),
    LOG("Log"), MILK("Milk"),
    NUTS("Nuts"), PLANT_FIBER("Plant Fiber"),
    SPICES("Spices"), VEGETABLE("Vegetable"),
    ADAMANTITE("Adamantite"), BOLITE("Bolite"),
    CRYSTALS("Crystals"), ELUDIUM("Eludium"),
    GEMS("Gems"), ICE("Ice"),
    LUMENITE("Lumenite"), MINERALS("Minerals"),
    ORE("Ore"), RADIOACTIVES("Radioactives"),
    STONE("Stone"), VULCANITE("Vulcanite"),
    AIR("Air"), CRYOZINE("Cryozine"),
    HYDROGEN("Hydrogen"), IOPLASMA("Ioplasma"),
    NATURAL_GAS("Natural Gas"), OIL("Oil"),
    PHLOGISTON("Phlogiston"), POLYTARIDE("Polytaride"),
    VIATHOL("Viathol"), FLOMENTUM("Flomentum"),
    MAGMEX("Magmex"), MYRATHANE("Myrathane"),
    WATER("Water"), TYPE_A_PREONS("Type A Preons"),
    TYPE_B_PREONS("Type B Preons"), TYPE_F_PREONS("Type F Preons"),
    TYPE_G_PREONS("Type G Preons"), TYPE_K_PREONS("Type K Preons"),
    TYPE_M_PREONS("Type M Preons"), TYPE_O_PREONS("Type O Preons"),
    COAL("Coal"), NULL("Null");

    private final String text;

    ResourceType(final String text) {
        this.text = text;
    }

    public static ResourceType getType(String in) {
        for (ResourceType type : ResourceType.values()) {
            if (type.text.equals(in)) return type;
        }
        return NULL;
    }


    @Override
    public String toString() {
        return text;
    }

    public static ArrayList<String> getAllNames() {
        ArrayList<String> names = new ArrayList<>();
        for (ResourceType type : values()) names.add(type.text);
        return names;
    }
}
