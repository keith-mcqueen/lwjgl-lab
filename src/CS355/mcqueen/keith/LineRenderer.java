package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import static org.lwjgl.opengl.GL11.*;

public class LineRenderer extends Renderer {
    @Override
    public void render(Point3D... points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required to render a line");
        }

        Point3D pointA = points[0];
        Point3D pointB = points[1];

        glBegin(GL_LINES);
        glVertex3d(pointA.x, pointA.y, pointA.z);
        glVertex3d(pointB.x, pointB.y, pointB.z);
        glEnd();
    }
}
