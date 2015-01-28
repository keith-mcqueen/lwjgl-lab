package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import static java.lang.Math.*;

public class Color extends Point3D {
    public Color(double r, double g, double b) {
        super(min(max(r, 0), 1), min(max(g, 0), 1), min(max(b, 0), 1));
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

    public Color times(Color that) {
        return new Color(this.red() * that.red(), this.green() * that.green(), this.blue() * that.blue());
    }

    public Color add(Color that) {
        Point3D sum = super.add(that);

        return new Color(sum.x, sum.y, sum.z);
    }
}
