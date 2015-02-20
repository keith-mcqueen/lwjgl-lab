package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;

import static CS355.mcqueen.keith.CameraController.*;

public class RayTracer {
    private Camera camera = null;

    public RayTracer(Camera camera, Scene scene) {
        this.camera = camera;
    }
}
