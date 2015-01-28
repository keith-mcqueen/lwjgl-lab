package CS355.mcqueen.keith;

import static org.lwjgl.opengl.GL11.*;

public class SurfaceRenderer extends Renderer {
    @Override
    public void render(Triangle3D triangle) {
        // begin triangles
        glBegin(GL_TRIANGLES);

        triangle.getVertices().forEach(v -> glVertex3d(v.x, v.y, v.z));

        // end triangles
        glEnd();
    }
}
