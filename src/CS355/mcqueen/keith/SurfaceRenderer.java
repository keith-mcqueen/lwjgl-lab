package CS355.mcqueen.keith;

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
    }
}
