package rfinder.Hazeron;

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
    WATER("Water");

    private final String text;

    ResourceType(final String text) {
        this.text = text;
    }
}
