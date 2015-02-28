package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class AmbientLightSource extends LightSource {
    public AmbientLightSource() {
        this(0.5);
    }

    public AmbientLightSource(double val) {
        this(val, val, val);
    }

    public AmbientLightSource(double r, double g, double b) {
        this(new Color(r, g, b));
    }

    public AmbientLightSource(Color color) {
        super(color);
    }

    @Override
    public Color getColorFor(Model3D model, Point3D location, Point3D eyeLocation) {
        Color modelColor = model.getColor();
        if (null == modelColor) {
            return new Color(0.0);
        }

        return modelColor.times(this.getColor());
    }
}
