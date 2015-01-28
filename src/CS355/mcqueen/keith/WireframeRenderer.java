package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class WireframeRenderer extends Renderer {
    @Override
    public void render(Triangle3D triangle) {
        // get the vertices from the triangle
        List<Point3D> vertices = triangle.getVertices();
        Point3D pointA = vertices.get(0);
        Point3D pointB = vertices.get(1);
        Point3D pointC = vertices.get(2);

        // begin lines
        glBegin(GL_LINES);

        // line AB
        glVertex3d(pointA.x, pointA.y, pointA.z);
        glVertex3d(pointB.x, pointB.y, pointB.z);

        // line BC
        glVertex3d(pointB.x, pointB.y, pointB.z);
        glVertex3d(pointC.x, pointC.y, pointC.z);

        // line CA
        glVertex3d(pointC.x, pointC.y, pointC.z);
        glVertex3d(pointA.x, pointA.y, pointA.z);

        // end lines
        glEnd();
    }

    @Override
    public void setColor(float r, float g, float b) {
        super.setColor(r, g, b);

        glColor3f(r, g, b);
    }
}
