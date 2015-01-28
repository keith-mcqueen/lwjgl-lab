package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class Triangle3D extends Model3D {
    public Triangle3D(Point3D pointA, Point3D pointB, Point3D pointC) {
        super.addVertex(pointA);
        super.addVertex(pointB);
        super.addVertex(pointC);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }
}
