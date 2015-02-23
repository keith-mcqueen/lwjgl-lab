package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.List;

public class Triangle3D extends Model3D {
    private Point3D centroid;
    private Point3D normal;

    public Triangle3D(Point3D pointA, Point3D pointB, Point3D pointC) {
        super(null);

        super.addVertex(pointA);
        super.addVertex(pointB);
        super.addVertex(pointC);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    @Override
    public Point3D getPointOfIntersection(Point3D origin, Point3D ray) {
        return null;
    }

    @Override
    public Point3D getNormal(Point3D point) {
        return this.normal;
    }

    public Point3D getCentroid() {
        return null != this.centroid ? this.centroid : this.computeCentroid();
    }

    private Point3D computeCentroid() {
        // the centroid of the triangle is the average of its vertices

        // add up all the vertices
        Point3D sum = new Point3D(0.0d, 0.0d, 0.0d);
        List<Point3D> vertices = this.getVertices();
        for (Point3D vertex : vertices) {
            sum = sum.add(vertex);
        }

        // divide by the number of vertices
        return this.centroid = sum.divide(vertices.size());
    }

    public Point3D getNormal() {
        return null != this.normal ? this.normal : this.computeNormal();
    }

    private Point3D computeNormal() {
        // the normal is computed by getting vectors for two edges, and normalizing their cross-product
        Point3D ab = this.getVertex(1).subtract(this.getVertex(0));
        Point3D ac = this.getVertex(2).subtract(this.getVertex(0));

        return this.normal = ab.cross(ac).normalize();
    }
}
