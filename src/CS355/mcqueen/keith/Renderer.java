package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import static org.lwjgl.opengl.GL11.*;

public abstract class Renderer {
    public abstract void render(Point3D...points);

    void setColor(float r, float g, float b) {
        glColor3f(r, g, b);
    }
}
