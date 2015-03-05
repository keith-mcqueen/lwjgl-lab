package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public abstract class PlaneModel extends Model3D {
    private static final Double ZERO = 0.0000000000001;

    private final Point3D centroid;
    private final Point3D normal;

    public PlaneModel(Point3D location, Point3D normal, Color color) {
        super(location, color);

        this.centroid = location;
        this.normal = normal;
    }

    @Override
    public Point3D getPointOfIntersection(Point3D r_o, Point3D r_d) {
        // first check to see if the ray intersects the plane at all

        r_d = r_d.normalize();
        Point3D a = this.getCentroid();
        Point3D n = this.getNormal();

        // if r_d dot n is 0, then that means the ray is parallel to the plane
        // and thus does not intersect it
        double r_d_dot_n = r_d.dot(n);
        if (Math.abs(r_d_dot_n) <= ZERO) {
            return null;
        }

        // compute the value of t
        double dot_product = (a.subtract(r_o)).dot(n);
        double t = dot_product / r_d_dot_n;
        if (t < 0.0) {
            return null;
        }

        // now compute the location of the intersection point
        Point3D intersectionPoint = r_o.add(r_d.times(t));

        return this.contains(intersectionPoint) ? intersectionPoint : null;
    }

    public Point3D getCentroid() {
        return this.centroid;
    }

    public Point3D getNormal() {
        return this.normal;
    }

    @Override
    public Point3D getNormal(Point3D point) {
        return this.normal;
    }

    protected abstract boolean contains(Point3D point);

    public static void main(String[] args) {
        Point3D poi;

        TestPlaneModel testModel = new TestPlaneModel(new Point3D(0.0), new Point3D(0.0, 1.0, 0.0), null);

        poi = testModel.getPointOfIntersection(new Point3D(1.0, 3.0, 1.0), new Point3D(0.0, -1.0, 0.0));
        System.out.println("poi = " + poi);

        poi = testModel.getPointOfIntersection(new Point3D(1.0, 3.0, 1.0), new Point3D(0.0, 1.0, 0.0));
        System.out.println("poi = " + poi);
    }

}

class TestPlaneModel extends PlaneModel {

    public TestPlaneModel(Point3D location, Point3D normal, Color color) {
        super(location, normal, color);
    }

    @Override
    protected boolean contains(Point3D point) {
        return true;
    }

    @Override
    public void render(Renderer renderer) {

    }
}
