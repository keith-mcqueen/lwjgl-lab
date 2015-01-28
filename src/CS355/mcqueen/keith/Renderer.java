package CS355.mcqueen.keith;

import static org.lwjgl.opengl.GL11.*;

public abstract class Renderer {
    public abstract void render(Triangle3D triangle);

    void setColor(float r, float g, float b) {
        glColor3f(r, g, b);
    }
}
