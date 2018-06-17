package rfinder.Hazeron;

public class Resource {
    private ResourceType type;
    private int zones, q1, q2, q3, a1, a2, a3;
    private String sphere;

    public Resource(ResourceType type, int zones, int q1, int q2, int q3, int a1, int a2, int a3, String sphere) {
        this.type = type;
        this.zones = zones;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.sphere = sphere;
    }
}
