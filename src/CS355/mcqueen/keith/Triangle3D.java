package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.List;

public class Triangle3D extends PlaneModel {
    public Triangle3D(Point3D a, Point3D b, Point3D c, Color color) {
        super(computeCentroid(a, b, c), computeNormal(a, b, c), color);

        super.addVertex(a);
        super.addVertex(b);
        super.addVertex(c);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    private static Point3D computeCentroid(Point3D a, Point3D b, Point3D c) {
        // the centroid of the triangle is the average of its vertices
        return a.add(b).add(c).divide(3.0);
    }

    private static Point3D computeNormal(Point3D a, Point3D b, Point3D c) {
        // the normal is computed by getting vectors for two edges, and normalizing their cross-product
        Point3D ab = b.subtract(a);
        Point3D ac = c.subtract(a);

        return ab.cross(ac).normalize();
    }

    @Override
    protected boolean contains(Point3D point) {
        // this algorithm is implemented from http://geomalgorithms.com/a06-_intersect-2.html

        // get 2 edge vectors
        Point3D u = this.getVertex(1).subtract(this.getVertex(0));
        Point3D v = this.getVertex(2).subtract(this.getVertex(0));

        // get a vector from the point to the vertex
        Point3D w = point.subtract(this.getVertex(0));

        // compute some dot products
        double u_dot_u = u.dot(u);
        double v_dot_v = v.dot(v);
        double u_dot_v = u.dot(v);
        double u_dot_v_squared = u_dot_v * u_dot_v;
        double w_dot_v = w.dot(v);
        double w_dot_u = w.dot(u);

        // compute parameter s
        double denominator = u_dot_v_squared - (u_dot_u * v_dot_v);
        double s = ((u_dot_v * w_dot_v) - (v_dot_v * w_dot_u)) / denominator;
        double t = ((u_dot_v * w_dot_u) - (u_dot_u * w_dot_v)) / denominator;

        return s >= 0.0 && t >= 0.0 && s + t <= 1;
    }

    public static void main(String[] args) {
        Triangle3D triangle =
                new Triangle3D(new Point3D(0.0, 0.0, 5.0), new Point3D(-5.0, 0.0, -5.0), new Point3D(5.0, 0.0, -5.0), null);
        boolean contains = triangle.contains(new Point3D(4.0, 0.0, 4.0));
        System.out.println("contains = " + contains);
    }
}
