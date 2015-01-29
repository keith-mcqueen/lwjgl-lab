package CS355.LWJGL;

/**
 * @author Brennan Smith
 */
public class Point3D {
    public final double x;
    public final double y;
    public final double z;

    public Point3D(double newX, double newY, double newZ) {
        x = newX;
        y = newY;
        z = newZ;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z:" + z;
    }

    public Point3D normalize() {
        return this.divide(this.length());
    }

    public Point3D add(Point3D that) {
        return new Point3D(this.x + that.x, this.y + that.y, this.z + that.z);
    }

    public Point3D divide(double divisor) {
        return new Point3D(this.x / divisor, this.y / divisor, this.z / divisor);
    }

    public Point3D subtract(Point3D that) {
        return new Point3D(this.x - that.x, this.y - that.y, this.z - that.z);
    }

    public Point3D cross(Point3D that) {
        return new Point3D(
                (this.y * that.z - this.z * that.y),
                (this.z * that.x - this.x * that.z),
                (this.x * that.y - this.y * that.x));
    }

    public double dot(Point3D that) {
        return (this.x * that.x) + (this.y * that.y) + (this.z * that.z);
    }

    public Point3D times(double scalar) {
        return new Point3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }
}
