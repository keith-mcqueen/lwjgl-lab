package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import static java.lang.Math.*;

public class Color extends Point3D {
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

    public Color times(Point3D that) {
        return new Color(this.x * that.x, this.y * that.y, this.z * that.z);
    }

    @Override
    public Color times(double scalar) {
        return new Color(super.times(scalar));
    }

    public Color add(Color that) {
        return new Color(super.add(that));
    }
}
