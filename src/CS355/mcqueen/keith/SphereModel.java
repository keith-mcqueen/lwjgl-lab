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
        super(color);

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
        DecimalFormat df = new DecimalFormat("#.###");

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Compute the point of intersection for the ray $\\vec {r_d} = " +
                        r_d + "$ originating from the point $r_o = " +
                        r_o + "$ and the sphere with radius $r = " +
                        df.format(this.radius) + "$ centered at $S_c = " + this.center + "$.");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println(this);
                System.out.println("r_o = " + r_o);
                System.out.println("r_d = " + r_d);
                System.out.println();
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        // Step 0 - Normalize r_d (if necessary)
        Point3D r_d_normalized = r_d.normalize();

        if (r_d.lengthSquared() == 1.0d) {
            switch (verboseness) {
                case VERBOSENESS_HIGH:
                    System.out.println("The ray vector $r_d$ is already normalized to a unit vector.");
                    System.out.println();
                    break;

                case VERBOSENESS_MEDIUM:
                case VERBOSENESS_LOW:
                    System.out.println("r_d is already normalized");
                    break;

                case VERBOSENESS_NONE:
                default:
                    break;
            }
        } else {
            switch (verboseness) {
                case VERBOSENESS_HIGH:
                    System.out.println("The ray vector $r_d$ needs to be normalized to a unit vector.");
                    System.out.println();
                    System.out.println("$$\\begin{aligned}");
                    System.out.println("\\hat {r_d} &= \\frac {\\vec {r_d}} {\\left|\\left| \\vec {r_d} \\right|\\right|} \\\\");
                    System.out.println("&= " + r_d_normalized.toString(AS_COLUMN_VECTOR));
                    System.out.println("\\end{aligned}$$");
                    System.out.println();
                    break;

                case VERBOSENESS_MEDIUM:
                case VERBOSENESS_LOW:
                    System.out.println("r_d_normalized = " + r_d_normalized);
                    break;

                case VERBOSENESS_NONE:
                default:
                    break;
            }
        }


        // Step 1 - Determine whether the r_d's origin is outside the sphere
        Point3D OC = this.center.subtract(r_o);
        double OC_length = OC.length();
        double oc_length_squared = OC.lengthSquared();

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Find $\\vec {OC}$ (the vector from the ray origin ($r_o$) to the sphere's center ($S_c$)):");
                System.out.println();
                System.out.println("$$\\begin{aligned}");
                System.out.println("\\vec {OC} &= \\vec {S_c} - \\vec {r_o} \\\\");
                System.out.println("&= " + this.center.toString(AS_COLUMN_VECTOR) + " - " + r_o.toString(AS_COLUMN_VECTOR) + " \\\\");
                System.out.println("&= " + OC.toString(AS_COLUMN_VECTOR));
                System.out.println("\\end{aligned}$$");
                System.out.println();

                System.out.println("Find the length of $\\vec {OC}$:");
                System.out.println();
                System.out.println("$$\\begin{aligned}");
                System.out.println("\\left|\\left| \\vec {OC} \\right|\\right|^2 &= " + OC.toString("(%2$s)^2 + (%3$s)^2 + (%4$s)^2") + " \\\\");
                System.out.println("&= " + df.format(oc_length_squared));
                System.out.println("\\end{aligned}$$");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("OC = " + this.center + " - " + r_o + " = " + OC);
                System.out.println("||OC|| = ||" + OC + "|| = " + OC_length);
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        boolean originIsInsideSphere = OC_length <= this.radius;

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Ray origin is " + (originIsInsideSphere ? "inside" : "outside") + " sphere, because $\\left|\\left| \\vec {OC} \\right|\\right|^2 " + (originIsInsideSphere ? "\\le" : "\\gt") + " r^2$.");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("Ray origin is " + (originIsInsideSphere ? "inside" : "outside") + " sphere");
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        // Step 2 - Find the closest approach of the r_d to the sphere's center
//        double t_ca = OC.dot(r_d);
        double t_ca = OC.dot(r_d_normalized);

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Find the value for $t$ that gives the closest approach to $S_c$ ($t_{ca}$):");
                System.out.println();
                System.out.println("$$\\begin{aligned}");
                System.out.println("t_{ca} &= \\vec {OC} \\cdot \\vec {r_d} \\\\");
                System.out.println("&= " + OC.toString(AS_COLUMN_VECTOR) + " \\cdot " + r_d.toString(AS_COLUMN_VECTOR) + " \\\\");
                System.out.println("&= " + df.format(t_ca));
                System.out.println("\\end{aligned}$$");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("OC . rd = " + OC + " . " + r_d + " = " + t_ca);
                System.out.println("t_ca = " + t_ca);
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        // Step 3 - Determine whether the r_d intersects the sphere
        if (t_ca < 0.0d) {
            switch (verboseness) {
                case VERBOSENESS_HIGH:
                    System.out.println("Ray does NOT intersect the sphere, because $t_{ca} \\lt 0$.");
                    System.out.println();
                    break;

                case VERBOSENESS_MEDIUM:
                case VERBOSENESS_LOW:
                    System.out.println("Ray does NOT intersect the sphere");
                    System.out.println();
                    break;

                case VERBOSENESS_NONE:
                default:
                    break;
            }
            return null;
        }

        // Step 4 - Compute the distance from the closest approach to the sphere's surface
        double radius_squared = this.radius * this.radius;
        double t_ca_squared = t_ca * t_ca;
        double t_hc_squared = radius_squared - oc_length_squared + t_ca_squared;

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Find the value for $t$ that gives the distance from the point of intersection on the sphere to the point of closest approach ($t_{hc}$):");
                System.out.println();
                System.out.println("$$\\begin{aligned}");
                System.out.println("t_{hc}^2 &= r^2 - \\left|\\left| \\vec {OC} \\right|\\right|^2 + t_{ca}^2 \\\\");
                System.out.println("&= (" + df.format(this.radius) + ")^2 - " + df.format(oc_length_squared) + " + (" + df.format(t_ca) + ")^2 \\\\");
                System.out.println("&= " + df.format(t_hc_squared) + " \\\\");
                System.out.println("\\end{aligned}$$");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("t_hc_squared = " + t_hc_squared);
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        // Step 5 - Determine whether the r_d intersects the sphere
        if (t_hc_squared < 0.0d) {
            switch (verboseness) {
                case VERBOSENESS_HIGH:
                    System.out.println("Ray does NOT intersect the sphere, because $t_{hc}^2 \\lt 0$.");
                    System.out.println();
                    break;

                case VERBOSENESS_MEDIUM:
                case VERBOSENESS_LOW:
                    System.out.println("Ray does NOT intersect the sphere");
                    System.out.println();
                    break;

                case VERBOSENESS_NONE:
                default:
                    break;
            }
            return null;
        }

        double t_hc = Math.sqrt(t_hc_squared);

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("$$\\begin{aligned}");
                System.out.println("t_{hc} &= " + df.format(t_hc));
                System.out.println("\\end{aligned}$$");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("t_hc = " + t_hc);
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        // Step 6 - Calculate the intersection distance
        double t = t_ca - ((originIsInsideSphere ? -1 : 1) * t_hc);

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Find the value for $t$ that gives the point of intersection on the sphere:");
                System.out.println();
                System.out.println("$$\\begin{aligned}");
                System.out.println("t &= t_{ca} + t_{hc} \\\\");
                System.out.println("&= " + df.format(t_ca) + (originIsInsideSphere ? " + " : " - ") + df.format(t_hc) + " \\\\");
                System.out.println("&= " + df.format(t));
                System.out.println("\\end{aligned}$$");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("t = " + t);
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        // Step 7 - Calculate the intersection point
        Point3D intersectionPoint = r_o.add(r_d_normalized.times(t));

        switch (verboseness) {
            case VERBOSENESS_HIGH:
                System.out.println("Find the point of intersection on the sphere:");
                System.out.println();
                System.out.println("$$\\begin{aligned}");
                System.out.println("\\vec {r(t)} &= \\vec {r_o} + \\hat {r_d}t \\\\");
                System.out.println("&= " + r_o.toString(AS_COLUMN_VECTOR) + " + " + df.format(t) + r_d_normalized.toString(AS_COLUMN_VECTOR) + " \\\\");
                System.out.println("&= " + intersectionPoint.toString(AS_COLUMN_VECTOR));
                System.out.println("\\end{aligned}$$");
                System.out.println();
                break;

            case VERBOSENESS_MEDIUM:
            case VERBOSENESS_LOW:
                System.out.println("intersectionPoint = " + r_o + " " + r_d_normalized + " * " + t + " = " + intersectionPoint);
                break;

            case VERBOSENESS_NONE:
            default:
                break;
        }

        return intersectionPoint;
    }

    public static void main(String[] args) {
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
    }
}
