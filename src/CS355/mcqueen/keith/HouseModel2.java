package CS355.mcqueen.keith;

import CS355.LWJGL.Line3D;
import CS355.LWJGL.Point3D;

/**
 * Created by keith on 1/26/15.
 */
public class HouseModel2 extends Model3D {
    @Override
    public void renderAsWireframe() {
        Renderer renderer = new LineRenderer();
        renderer.setColor(1.0f, 1.0f, 0.0f);

        //Make the object:
        //Floor
        renderer.render(new Point3D(-5, 0, -5), new Point3D(5, 0, -5));
        renderer.render(new Point3D(5, 0, -5), new Point3D(5, 0, 5));
        renderer.render(new Point3D(5, 0, 5), new Point3D(-5, 0, 5));
        renderer.render(new Point3D(-5, 0, 5), new Point3D(-5, 0, -5));
        //Ceiling
        renderer.render(new Point3D(-5, 5, -5), new Point3D(5, 5, -5));
        renderer.render(new Point3D(5, 5, -5), new Point3D(5, 5, 5));
        renderer.render(new Point3D(5, 5, 5), new Point3D(-5, 5, 5));
        renderer.render(new Point3D(-5, 5, 5), new Point3D(-5, 5, -5));
        //Walls
        renderer.render(new Point3D(-5, 5, -5), new Point3D(-5, 0, -5));
        renderer.render(new Point3D(5, 5, -5), new Point3D(5, 0, -5));
        renderer.render(new Point3D(5, 5, 5), new Point3D(5, 0, 5));
        renderer.render(new Point3D(-5, 5, 5), new Point3D(-5, 0, 5));
        //Roof
        renderer.render(new Point3D(-5, 5, -5), new Point3D(0, 8, -5));
        renderer.render(new Point3D(0, 8, -5), new Point3D(5, 5, -5));
        renderer.render(new Point3D(-5, 5, 5), new Point3D(0, 8, 5));
        renderer.render(new Point3D(0, 8, 5), new Point3D(5, 5, 5));
        renderer.render(new Point3D(0, 8, -5), new Point3D(0, 8, 5));
        //Door
        renderer.render(new Point3D(1, 0, 5), new Point3D(1, 3, 5));
        renderer.render(new Point3D(-1, 0, 5), new Point3D(-1, 3, 5));
        renderer.render(new Point3D(1, 3, 5), new Point3D(-1, 3, 5));
    }

    @Override
    public void renderAsSolid() {

    }
}
