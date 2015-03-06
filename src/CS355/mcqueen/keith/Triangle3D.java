package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.awt.*;
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
    public Point3D getNormal(Point3D point) {
        // adapted from http://mathforum.org/library/drmath/view/62814.html

        // get the vertices
        Point3D a = this.getVertex(0);
        Point3D b = this.getVertex(1);
        Point3D c = this.getVertex(2);

        // make them 1st class vertices (if possible)
        Vertex p1, p2, p3;
        Point3D n1, n2, n3;
        p1 = p2 = p3 = null;
        n1 = n2 = n3 = null;
        if (a instanceof Vertex) {
            p1 = (Vertex) a;
            n1 = p1.getNormal();
        }
        if (b instanceof Vertex) {
            p2 = (Vertex) b;
            n2 = p2.getNormal();
        }
        if (c instanceof Vertex) {
            p3 = (Vertex) c;
            n3 = p3.getNormal();
        }

        // make sure we have all vertices
        if (null == p1 || null == p2 || null == p3 || null == n1 || null == n2 || null == n3) {
            return super.getNormal(point);
        }

        // compute the point of intersection of the vector from p1 to the given point and the edge formed by p2 and p3
        Point3D v1 = point.subtract(p1).normalize();
        Point3D p2_minus_p1 = p2.subtract(p1).normalize();
        Point3D v2 = p3.subtract(p2).normalize();

        double t = p2_minus_p1.cross(v2).length() / v1.cross(v2).length();

        Point3D p_i = p1.add(v1.times(t));

        // interpolate the normal between p2 and p3 for point of intersection
        Point3D n_p_i = n2.add(n3.subtract(n2).times(p_i.subtract(p2).length() / p3.subtract(p2).length()));

        // interpolate the normal between p1 and the point of intersection for the final normal
        Point3D n_p = n1.add(n_p_i.subtract(n1).times(point.subtract(p1).length() / p_i.subtract(p1).length()));

        return n_p;
//        return super.getNormal(point);
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
