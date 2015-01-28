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

    public abstract Color getColorFor(Point3D location, Point3D normal, Color reflectiveColor);

    protected Color getColor() {
        return this.color;
    }
}
