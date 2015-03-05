package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.text.DecimalFormat;
import java.util.Random;
import static CS355.LWJGL.Point3D.randomPoint;
import static CS355.mcqueen.keith.Color.randomColor;

public class SphereModel extends Model3D {
    public static final int VERBOSENESS_NONE = 0;
    public static final int VERBOSENESS_LOW = 1;
    public static final int VERBOSENESS_MEDIUM = 2;
    public static final int VERBOSENESS_HIGH = 3;

    private static final String AS_COLUMN_VECTOR = "\\left [ \\array { %2$s \\cr %3$s \\cr %4$s } \\right ]";

    private final Point3D center;
    private final double radius;

    public static SphereModel randomSphere() {
        return new SphereModel(randomPoint(100.0d), 10.0d, randomColor());
    }

    public SphereModel(double centerX, double centerY, double centerZ, double radius, Color color) {
        this(new Point3D(centerX, centerY, centerZ), radius, color);
    }

    public SphereModel(Point3D center, double radius, Color color) {
        super(center, color);

        this.center = center;
        this.radius = radius;
    }

    @Override
    public void render(Renderer renderer) {

    }

    public String toString() {
        return "Sphere with radius " + this.radius + ", centered at " + this.center;
    }

    @Override
    public Point3D getPointOfIntersection(Point3D origin, Point3D ray) {
        return this.getPointOfIntersection(origin, ray, VERBOSENESS_NONE);
    }

    @Override
    public Point3D getNormal(Point3D point) {
        if (null == point) {
            throw new IllegalArgumentException("Point must not be null");
        }

        return point.subtract(this.center).normalize();
    }

    private Point3D getPointOfIntersection(Point3D r_o, Point3D r_d, int verboseness) {
        // Step 0 - Normalize r_d (if necessary)
        Point3D r_d_normalized = r_d.normalize();

        // Step 1 - Determine whether the r_d's origin is outside the sphere
        Point3D OC = this.center.subtract(r_o);
        double OC_length = OC.length();
        double oc_length_squared = OC.lengthSquared();
        boolean originIsInsideSphere = OC_length <= this.radius;

        // Step 2 - Find the closest approach of the r_d to the sphere's center
        double t_ca = OC.dot(r_d_normalized);

        // Step 3 - Determine whether the r_d intersects the sphere
        if (t_ca < 0.0d) {
            return null;
        }

        // Step 4 - Compute the distance from the closest approach to the sphere's surface
        double radius_squared = this.radius * this.radius;
        double t_ca_squared = t_ca * t_ca;
        double t_hc_squared = radius_squared - oc_length_squared + t_ca_squared;

        // Step 5 - Determine whether the r_d intersects the sphere
        if (t_hc_squared < 0.0d) {
            return null;
        }

        // Step 6 - Calculate the intersection distance
        double t_hc = Math.sqrt(t_hc_squared);
        double t = t_ca - ((originIsInsideSphere ? -1 : 1) * t_hc);

        // Step 7 - Calculate the intersection point
        Point3D intersectionPoint = r_o.add(r_d_normalized.times(t));

        return intersectionPoint;
    }

    @Override
    public double getRefractionRatio(Point3D location, Point3D direction) {
        double n = this.getIndexOfRefraction();
        if (n <= 0.0) {
            return 0.0;
        }

        Point3D toCenter = this.center.subtract(location);
        if (toCenter.dot(direction) > 0.0) {
            return 1.0 / n;
        }

        return n;
    }

    public static void main(String[] args) {
        /*
        Random random = new Random(System.currentTimeMillis());
        int bound = 11;

        // initialize the camera
        Camera camera = new Camera(random.nextInt(bound), random.nextInt(bound), random.nextInt(bound), 1.0d);
//        Camera camera = new Camera(5.0d, 0.0d, 6.0d);

        // initialize the scene
//        SphereModel sphere = new SphereModel(random.nextInt(bound), random.nextInt(bound), random.nextInt(bound), random.nextInt(bound));
//        SphereModel sphere = new SphereModel(0.0d, 18.0d, 18.0d, 15.0d);
        SphereModel sphere = SphereModel.randomSphere();

        // initialize a ray
        Point3D ray = new Point3D(random.nextInt(2), random.nextInt(2), random.nextInt(2));
//        Point3D ray = new Point3D(0.0d, 0.0d, 1.0d).normalize();

        // calculate intersection of the ray with the sphere
        sphere.getPointOfIntersection(camera, ray, VERBOSENESS_HIGH);

        System.out.println("The following is the output generated by my `SphereModel::getPointOfIntersection()` method:");
        System.out.println();
        System.out.println("```");
        sphere.getPointOfIntersection(camera, ray, VERBOSENESS_LOW);
        System.out.println("```");
        */

        SphereModel sphere = new SphereModel(new Point3D(0.0), 10.0, null);
        Point3D intersection = sphere.getPointOfIntersection(new Point3D(0.0, 0.0, -11.0), new Point3D(0.0, 0.0, 1.0));
        System.out.println("intersection = " + intersection);
    }
}
