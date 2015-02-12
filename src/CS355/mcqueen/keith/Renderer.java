package CS355.mcqueen.keith;

import static org.lwjgl.opengl.GL11.*;

public abstract class Renderer {
    private Color baseColor;

    protected Renderer(boolean enableDepthTesting) {
        if (enableDepthTesting) {
            // configure the depth (Z) buffer
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);

        }
    }

    public abstract void render(Triangle3D triangle);

    public void setColor(float r, float g, float b) {
        this.setColor(new Color(r, g, b));
    }

    public void setColor(Color color) {
        this.baseColor = color;
    }

    public Color getColor() {
        return this.baseColor;
    }
}
