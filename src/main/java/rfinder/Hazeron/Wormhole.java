package rfinder.Hazeron;

public class Wormhole {
    private double destX, destY, destZ;

    public Wormhole(double destX, double destY, double destZ) {
        this.destX = destX;
        this.destY = destY;
        this.destZ = destZ;
    }

    public double getDestX() {
        return destX;
    }

    public double getDestY() {
        return destY;
    }

    public double getDestZ() {
        return destZ;
    }
}
