package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public abstract class LightSource {
    private Color color;

    public LightSource(float r, float g, float b) {
        this(new Color(r, g, b));
    }

    public LightSource(Color color) {
        this.color = color;
    }

    public abstract Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation);

    protected Color getColor() {
        return this.color;
    }
}
