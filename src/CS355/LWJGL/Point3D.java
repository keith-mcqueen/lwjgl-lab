package CS355.LWJGL;

import java.text.DecimalFormat;

/**
 * @author Brennan Smith
 */
public class Point3D {
    public static final String DEFAULT_FORMAT = "(%2$s, %3$s, %4$s)";

    public  double x;
    public  double y;
    public  double z;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    public Point3D(double newX, double newY, double newZ) {
        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    @Override
    public String toString() {
        return this.toString(DEFAULT_FORMAT);
    }

    public String toString(String format) {
        return String.format(format, this.getClass().getSimpleName(),
                DECIMAL_FORMAT.format(this.x), DECIMAL_FORMAT.format(this.y), DECIMAL_FORMAT.format(this.z));
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

    public Point3D times(Point3D that) {
        return new Point3D(this.x * that.x, this.y * that.y, this.z * that.z);
    }

    public static void main(String[] args) {
        Point3D a = new Point3D(-5, 18, 12);
        Point3D b = new Point3D(1, 0, 1);

        double dotProduct = a.dot(b);

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("dotProduct = " + dotProduct);
    }
}
