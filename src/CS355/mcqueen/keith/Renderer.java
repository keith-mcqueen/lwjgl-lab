package CS355.mcqueen.keith;

public abstract class Renderer {
    private Color baseColor;

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
