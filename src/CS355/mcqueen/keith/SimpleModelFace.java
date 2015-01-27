package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class SimpleModelFace extends Model3D {

    @Override
    public void addVertex(Point3D vertex) {
        super.addVertex(vertex);
    }

    @Override
    public void renderAsWireframe() {
        Renderer renderer = new LineRenderer();
        renderer.setColor(1.0f, 0.0f, 0.0f);

        Point3D first = null;
        Point3D previous = null;
        for (Point3D next : this.getVertices()) {
            if (null == first) {
                first = next;
            }

            if (null != previous) {
                renderer.render(previous, next);
            }

            previous = next;
        }

        // close the "box"
        renderer.render(previous, first);
    }

    @Override
    public void renderAsSolid() {

    }
}
