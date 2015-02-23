package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.ArrayList;
import java.util.List;

public class TriangleModel extends Model3D {
    private List<Triangle3D> triangles = new ArrayList<>();

    public TriangleModel(Color color) {
        super(color);
    }

    protected boolean addTriangle(Triangle3D t) {
        return this.triangles.add(t);
    }

    @Override
    public void render(Renderer renderer) {
        this.getTriangles().forEach(renderer::render);
    }

    @Override
    public Point3D getPointOfIntersection(Point3D origin, Point3D ray) {
        for (Triangle3D triangle : this.triangles) {
            Point3D poi = triangle.getPointOfIntersection(origin, ray);
            if (null != poi) {
                return poi;
            }
        }

        return null;
    }

    @Override
    public Point3D getNormal(Point3D point) {
        return null;
    }

    public List<Triangle3D> getTriangles() {
        return new ArrayList<>(this.triangles);
    }
}
