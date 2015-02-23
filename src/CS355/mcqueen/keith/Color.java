package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static java.lang.Math.*;

public class Color extends Point3D {
    public static Color randomColor() {
        return new Color(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble());
    }

    public Color(double r, double g, double b) {
        super(min(max(r, 0), 1), min(max(g, 0), 1), min(max(b, 0), 1));
    }

    public Color(Point3D color) {
        this(color.x, color.y, color.z);
    }

    public double red() {
        return super.x;
    }

    public double green() {
        return super.y;
    }

    public double blue() {
        return super.z;
    }

    @Override
    public Color times(Point3D that) {
        return new Color(this.x * that.x, this.y * that.y, this.z * that.z);
    }

    @Override
    public Color times(double scalar) {
        return new Color(super.times(scalar));
    }

    @Override
    public Color add(Point3D that) {
        return new Color(super.add(that));
    }

    public int toInteger() {
        int result = 0;

        // add the red
        result |= ((int) (this.red() * 255)) << 16;

        // add the green
        result |= ((int) (this.green() * 255)) << 8;

        // add the blue
        result |= ((int) (this.blue() * 255)) << 0;

        return result;
    }
}
