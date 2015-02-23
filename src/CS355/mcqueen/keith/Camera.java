package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class Camera extends Point3D {
    public static final Point3D DEFAULT_UP = new Point3D(0.0d, 1.0d, 0.0d);
    public static final Point3D DEFAULT_LOOK_AT = new Point3D(0.0d, 0.0d, -1.0d);
    public static final double DEFAULT_FOV_DEGREES = 45.0d;

    private final double focalLength;
    private final Point3D up;
    private final Point3D lookAt;
    private final double fovDegrees;

    public Camera(double focalLength) {
        this(0.0d, 0.0d, 0.0d, focalLength);
    }

    public Camera(double x, double y, double z, double focalLength) {
        super(x, y, z);

        this.focalLength = focalLength;

        this.fovDegrees = DEFAULT_FOV_DEGREES;

        // default up is the (positive) Y-axis
        this.up = DEFAULT_UP;

        // default look-at is the (negative) Z-axis
        this.lookAt = DEFAULT_LOOK_AT;
    }

    public double getFieldOfViewInDegrees() {
        return this.fovDegrees;
    }

    public double getFocalLength() {
        return this.focalLength;
    }
}
