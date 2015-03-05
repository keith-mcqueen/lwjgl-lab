package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.Random;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.PI;

public class AreaLightSource extends LightSource {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final double SAMPLING_RAYS = 35.0;

    private final Point3D location;
    private final double radius;
    private final Point3D normal;

    private final PointLightSource pointSource;
    private final Point3D radial;

    public AreaLightSource(Point3D location, double radius, Point3D normal, Color color) {
        super(color);

        this.location = location;
        this.radius = radius;
        this.normal = normal;

        this.radial = computeRadialVector(this.normal);

        this.pointSource = new PointLightSource(this.location, color);
    }

    private static Point3D computeRadialVector(Point3D normal) {
        // cross the given normal vector with the axis vectors until we find one that works
        Point3D[] axes = new Point3D[] {
                new Point3D(1.0, 0.0, 0.0),     // x-axis
                new Point3D(0.0, 1.0, 0.0),     // y-axis
                new Point3D(0.0, 0.0, 1.0) };   // z-axis

        for (Point3D axis : axes) {
            Point3D radial = normal.cross(axis);
            if (radial.lengthSquared() > 0.0) {
                return radial.normalize();
            }
        }

        return null;
    }

    @Override
    public Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation, Iterable<Model3D> obstacles) {
        Point3D compositeColor = new Point3D(0.0);

        for (int i = 0; i < SAMPLING_RAYS; i++) {
            this.pointSource.setLocation(this.location.subtract(this.getRandomLocation()));

            compositeColor = compositeColor.add(this.pointSource.getColorFor(model, location, eyeLocation, obstacles));
        }

        return new Color(compositeColor.divide(SAMPLING_RAYS));
    }

    private Point3D getRandomLocation() {
        Point3D v = this.radial;
        if (null == v) {
            throw new IllegalStateException("No radial vector");
        }

        // the following is an implementation of Rodrigues's Formula (see http://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula)

        // generate a random angle for rotation
        double theta = RANDOM.nextDouble() * PI * 2;

        double cos_theta = cos(theta);
        Point3D v_cos_theta = v.times(cos_theta);
        Point3D k = this.normal;
        Point3D k_cross_v = k.cross(v);
//        double k_dot_v = k.dot(v);

        Point3D v_rot = v_cos_theta.add(k_cross_v.times(sin(theta)));//.add(k.times(k_dot_v).times(1 - cos_theta));

        return v_rot.times(RANDOM.nextDouble() * this.radius);
    }
}
