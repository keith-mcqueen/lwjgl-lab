package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class Vertex extends Point3D {
    private Point3D normal;

    public Vertex(double newX, double newY, double newZ) {
        this(newX, newY, newZ, null);
    }

    public Vertex(Point3D location, Point3D normal) {
        this(location.x, location.y, location.z, normal);
    }

    public Vertex(double x, double y, double z, Point3D normal) {
        super(x, y, z);

        this.normal = normal;
    }

    public Point3D getNormal() {
        return normal;
    }
}
