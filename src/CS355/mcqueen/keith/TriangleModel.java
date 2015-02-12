package CS355.mcqueen.keith;

import java.util.ArrayList;
import java.util.List;

public class TriangleModel extends Model3D {
    private List<Triangle3D> triangles = new ArrayList<>();

    protected boolean addTriangle(Triangle3D t) {
        return this.triangles.add(t);
    }

    @Override
    public void render(Renderer renderer) {
        this.getTriangles().forEach(renderer::render);
    }

    public List<Triangle3D> getTriangles() {
        return new ArrayList<>(this.triangles);
    }
}
