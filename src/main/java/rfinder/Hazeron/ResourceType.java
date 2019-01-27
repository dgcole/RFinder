package rfinder.Hazeron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public enum ResourceType {

    ANIMAL_CARCASS("Animal Carcass", "Animal Carcass"),
    BEANS("Beans", "Beans"),
    CHEESE("Cheese", "Cheese"),
    EGGS("Eggs", "Eggs"),
    FERTILIZER("Fertilizer", "Fertilizer"),
    FISH("Fish", "Fish"),
    FRUIT("Fruit", "Fruit"),
    GRAIN("Grain", "Grain"),
    GRAPES("Grapes", "Grapes"),
    HERBS("Herbs", "Herbs"),
    HOPS("Hops", "Hops"),
    HAY("Hay", "Hay"),
    LOG("Log", "Log"),
    MILK("Milk", "Milk"),
    NUTS("Nuts", "Nuts"),
    PLANT_FIBER("Plant Fiber", "Plant Fiber"),
    SPICES("Spices", "Spices"),
    VEGETABLE("Vegetable", "Vegetable"),
    ADAMANTITE("Adamantite", "Adamantite"),
    BOLITE("Bolite", "Bolite"),
    CRYSTALS("Crystals", "Crystals"),
    ELUDIUM("Eludium", "Eludium"),
    GEMS("Gems", "Gems"),
    ICE("Ice", "Ice"),
    LUMENITE("Lumenite", "Lumenite"),
    MINERALS("Minerals", "Minerals"),
    ORE("Ore", "Ore"),
    RADIOACTIVES("Radioactives", "Radioactives"),
    STONE("Stone", "Stone"),
    VULCANITE("Vulcanite", "Vulcanite"),
    AIR("Air", "Air"),
    CRYOZINE("Cryozine", "Cryozine"),
    HYDROGEN("Hydrogen", "Hydrogen"),
    IOPLASMA("Ioplasma", "Ioplasma"),
    NATURAL_GAS("Natural Gas", "Natural Gas"),
    OIL("Oil", "Oil"),
    PHLOGISTON("Phlogiston", "Phlogiston"),
    POLYTARIDE("Polytaride", "Polytaride"),
    VIATHOL("Viathol", "Viathol"),
    FLOMENTUM("Flomentum", "Flomentum"),
    MAGMEX("Magmex", "Magmex"),
    MYRATHANE("Myrathane", "Myrathane"),
    WATER("Water in the Environment", "Water"),
    TYPE_A_PREONS("Type A Preons", "Type A Preons"),
    TYPE_B_PREONS("Type B Preons", "Type B Preons"),
    TYPE_F_PREONS("Type F Preons", "Type F Preons"),
    TYPE_G_PREONS("Type G Preons", "Type G Preons"),
    TYPE_K_PREONS("Type K Preons", "Type K Preons"),
    TYPE_M_PREONS("Type M Preons", "Type M Preons"),
    TYPE_O_PREONS("Type O Preons", "Type O Preons"),
    COAL("Coal", "Coal"),
    VEGETATION("Vegetation Density", "Vegetation Density"),
    ANY("Any", "Any"),
    BOREXINO_PRECIPITATE("Borexino Precipitate", "Borexino Precipitate"),
    ANTIFLUX_PARTICLES("Antiflux Particles", "Antiflux Particles");

    private final String internalName;
    private final String displayName;
    private static final ArrayList<ResourceType> sortedResources = new ArrayList<>(Arrays.asList(values()));
    static {
        sortedResources.sort(Comparator.comparing(ResourceType::toString));
        sortedResources.remove(ANY);
    }
    private static final HashMap<String, ResourceType> resourceMap = new HashMap<>();
    static {
        for (ResourceType r : sortedResources) {
            resourceMap.put(r.internalName, r);
        }
    }

    ResourceType(final String internalName, final String displayName) {
        this.internalName = internalName;
        this.displayName = displayName;
    }

    public static ResourceType getType(String in) {
        ResourceType retrieved = resourceMap.get(in);
        return retrieved == null ? ANY : retrieved;
    }


    @Override
    public String toString() {
        return displayName;
    }

    public static ArrayList<ResourceType> getTypes() {
        return sortedResources;
    }

    public static int indexOf(ResourceType resourceType) {
        return sortedResources.indexOf(resourceType);
    }
}
