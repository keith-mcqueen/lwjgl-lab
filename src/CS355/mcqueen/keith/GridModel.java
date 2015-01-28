package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

public class GridModel extends Model3D {
    private int min;
    private int max;
    private int step;

    public GridModel(int min, int max, int step) {
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void render(Renderer renderer) {
        for (int x = this.min; x <= this.max; x += this.step) {
            renderer.render(new Triangle3D(new Point3D(x, 0, this.min), new Point3D(x, 0, this.max), new Point3D(x, 0, this.max)));
        }
        for (int z = this.min; z <= this.max; z += this.step) {
            renderer.render(new Triangle3D(new Point3D(this.min, 0, z), new Point3D(this.max, 0, z), new Point3D(this.max, 0, z)));
        }
    }
}
