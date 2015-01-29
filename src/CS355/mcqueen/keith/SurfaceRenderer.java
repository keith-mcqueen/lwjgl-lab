package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static org.lwjgl.opengl.GL11.*;

public class SurfaceRenderer extends Renderer {
    private Shader shader;

    public SurfaceRenderer(Shader shader) {
        this.shader = shader;

        // configure the depth (Z) buffer
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
    }

    @Override
    public void render(Triangle3D triangle) {
        this.shader.shade(triangle, this.getColor());

        // render the normal
//        this.renderFaceNormal(triangle);
    }

    private void renderFaceNormal(Triangle3D triangle) {
        glColor3d(1.0, 1.0, 1.0);
        glBegin(GL_LINES);

        // get the triangle's centroid
        Point3D pointA = triangle.getCentroid();
        glVertex3d(pointA.x, pointA.y, pointA.z);

        Point3D pointB = pointA.add(triangle.getNormal().times(5.0));
        glVertex3d(pointB.x, pointB.y, pointB.z);

        glEnd();
    }
}
