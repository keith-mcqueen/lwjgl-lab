package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class AmbientLightSource extends LightSource {
    public AmbientLightSource() {
        this(0.5f, 0.5f, 0.5f);
    }

    public AmbientLightSource(float r, float g, float b) {
        this(new Color(r, g, b));
    }

    public AmbientLightSource(Color color) {
        super(color);
    }

    @Override
    public Color getColorFor(Point3D location, Point3D normal, Color reflectiveColor) {
        return reflectiveColor.times(this.getColor());
    }
}
