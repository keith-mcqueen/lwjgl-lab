package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import CS355.LWJGL.StudentLWJGLController;
import org.lwjgl.opengl.Display;

import java.text.MessageFormat;

import static CS355.LWJGL.LWJGLSandbox.DISPLAY_HEIGHT;
import static CS355.LWJGL.LWJGLSandbox.DISPLAY_WIDTH;
import static java.lang.Math.*;
import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * My controller class.
 * <p>
 * Created by keith on 5/31/14.
 */
public class CameraController extends StudentLWJGLController {
    public static final float FIELD_OF_VIEW = 45.0f;
    public static final float ASPECT = (float) DISPLAY_WIDTH / (float) DISPLAY_HEIGHT;
    public static final float Z_NEAR = 1.0f;
    public static final float Z_FAR = 500.0f;

    public static final float INITIAL_X = 0.0f;
    public static final float INITIAL_Y = -3.0f;
    public static final float INITIAL_Z = -20.0f;

    public static final float WALK_DISTANCE = 0.1f;
    public static final float ROTATION_ANGLE = 1.0f;
    public static final double _90_DEGREES = 90;

    // position of the camera (really the world)
    private Camera camera = new Camera(INITIAL_X, INITIAL_Y, INITIAL_Z);

    // orientation of the camera (the world, really) about the Y axis in radians
    private float yaw = 0.0f;

    private final GridModel gridModel = new GridModel(-100, 100, 10);
    private Model3D model;
    private Renderer renderer;
    private int numSlices = 2;
    private Axis sliceAxis = Axis.Y;
    private AbstractFileModel inputModel;

    public CameraController(String[] args) {
        if (args.length > 0) {
            String modelFile = args[0];
            if (modelFile.endsWith(".obj")) {
                this.inputModel = new ObjFileModel(modelFile);
            } else if (modelFile.endsWith(".stl")) {
                this.inputModel = new StlFileModel(modelFile);
            }

            this.model = new SlicedModel(this.inputModel, this.sliceAxis, this.numSlices);
//            this.model = fileModel;
        }
    }

    private void init() {
//        this.initSurfaceRenderer();
        this.initWireframeRenderer();
    }

    private void initWireframeRenderer() {
        this.renderer = new WireframeRenderer(false);
        this.renderer.setColor(0.0f, 1.0f, 0.0f);
    }

    private void initSurfaceRenderer() {
        // ambient light source
        AmbientLightSource a = new AmbientLightSource(new Color(0.4, 0.4, 0.4));

        // directional light source
        DirectionalLightSource d = new DirectionalLightSource(new Color(0.4, 0.4, 0.4), new Point3D(1.0, 1.0, 1.0));

        // point light source(s)
        PointLightSource p1 = new PointLightSource(new Color(6.0, 0.0, 0.0), new Point3D(80.0, 80.0, 80.0));
        PointLightSource p2 = new PointLightSource(new Color(0.0, 6.0, 0.0), new Point3D(-80.0, -80.0, -80.0));
        PointLightSource p3 = new PointLightSource(new Color(0.0, 0.0, 6.0), new Point3D(0.0, 80.0, 80.0));

        // moving point light sources
//        MovingPointLightSource m1 = new MovingPointLightSource(new Color(0.6, 0.0, 0.0), new Point3D(0.0, 0.0, 0.0));
//        MovingPointLightSource m2 = new MovingPointLightSource(new Color(0.0, 0.6, 0.0), new Point3D(0.0, 0.0, 0.0));
//        MovingPointLightSource m3 = new MovingPointLightSource(new Color(0.0, 0.0, 0.6), new Point3D(0.0, 0.0, 0.0));

        // shader
//        FlatShader shader = new FlatShader(a, d, p1, p2, p3, m1, m2, m3);
        FlatShader shader = new FlatShader(a, d, p1, p2, p3);

        // renderer
        this.renderer = new SurfaceRenderer(shader);
        this.renderer.setColor(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void resizeGL() {
        // set the clear color (black)
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // now clear the buffer
        glClear(GL_COLOR_BUFFER_BIT);

        // set up the viewport
        glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        // start in perspective mode
        this.perspectiveMode();
    }

    @Override
    public void update() {
        // no op
    }

    @Override
    public void updateKeyboard() {
        if (isKeyDown(KEY_W) || isKeyDown(KEY_UP)) {

            // walk 1 unit forward
            this.forward(WALK_DISTANCE);

        } else if (isKeyDown(KEY_S) || isKeyDown(KEY_DOWN)) {

            // walk 1 unit backward
            this.back(WALK_DISTANCE);

        } else if (isKeyDown(KEY_A) || isKeyDown(KEY_LEFT)) {

            // walk 1 unit to the left
            this.left(WALK_DISTANCE);

        } else if (isKeyDown(KEY_D) || isKeyDown(KEY_RIGHT)) {

            // walk 1 unit to the right
            this.right(WALK_DISTANCE);

        } else if (isKeyDown(KEY_Q)) {

            // turn left
            this.turn(-ROTATION_ANGLE);

        } else if (isKeyDown(KEY_E)) {

            // turn right
            this.turn(ROTATION_ANGLE);

        } else if (isKeyDown(KEY_R)) {

            // fly up
            this.fly(-WALK_DISTANCE);

        } else if (isKeyDown(KEY_F)) {

            // fly down
//			this.fly(dt * MOVEMENT_SPEED);
            this.fly(WALK_DISTANCE);

        } else if (isKeyDown(KEY_O)) {

            // switch to orthographic mode
            this.orthographicMode();

        } else if (isKeyDown(KEY_P)) {

            // switch to perspective mode
            this.perspectiveMode();

        } else if (isKeyDown(KEY_H)) {

            // reset back to the original state
            this.camera.x = INITIAL_X;
            this.camera.y = INITIAL_Y;
            this.camera.z = INITIAL_Z;
            this.yaw = 0.0f;

        } else if (isKeyDown(KEY_EQUALS)) {
            this.model = new SlicedModel(this.inputModel, this.sliceAxis, ++this.numSlices);
        } else if (isKeyDown(KEY_MINUS)) {
            this.model = new SlicedModel(this.inputModel, this.sliceAxis, --this.numSlices);
        } else if (isKeyDown(KEY_X)) {
            this.sliceAxis = Axis.X;
            this.model = new SlicedModel(this.inputModel, this.sliceAxis, this.numSlices);
        } else if (isKeyDown(KEY_Y)) {
            this.sliceAxis = Axis.Y;
            this.model = new SlicedModel(this.inputModel, this.sliceAxis, this.numSlices);
        } else if (isKeyDown(KEY_Z)) {
            this.sliceAxis = Axis.Z;
            this.model = new SlicedModel(this.inputModel, this.sliceAxis, this.numSlices);
        }

        Display.setTitle(MessageFormat.format("BYU CS 455: Slice axis: {0}; Slices: {1}", this.sliceAxis, this.numSlices + 1));
    }

    @Override
    public void render() {
        // super calls glClear() for us
        super.render();

        if (null == this.renderer) {
            this.init();
        }

        // update the camera position
        this.lookThrough();

        // render the "floor" grid
//        this.gridModel.render(renderer);

        // render the obj-file model if there is one
        if (null != this.model) {
            this.model.render(this.renderer);
        }
    }

    private void lookThrough() {
        // "reset" all transformations
        glLoadIdentity();

        // translate
        glTranslated(this.camera.x, this.camera.y, this.camera.z);

        // rotate about the y axis
        glRotatef(this.yaw, 0.0f, 1.0f, 0.0f);
    }

    private void forward(float distance) {
        this.camera.x -= distance * (float) sin(toRadians(this.yaw));
        this.camera.z += distance * (float) cos(toRadians(this.yaw));
    }

    private void back(float distance) {
        this.camera.x += distance * (float) sin(toRadians(this.yaw));
        this.camera.z -= distance * (float) cos(toRadians(this.yaw));
    }

    private void left(float distance) {
        this.camera.x -= distance * (float) sin(toRadians(this.yaw - _90_DEGREES));
        this.camera.z += distance * (float) cos(toRadians(this.yaw - _90_DEGREES));
    }

    private void right(float distance) {
        this.camera.x -= distance * (float) sin(toRadians(this.yaw + _90_DEGREES));
        this.camera.z += distance * (float) cos(toRadians(this.yaw + _90_DEGREES));
    }

    private void fly(float distance) {
        this.camera.y += distance;
    }

    private void turn(float amount) {
        this.yaw += amount;
    }

    private void perspectiveMode() {
        glMatrixMode(GL_PROJECTION);

        glLoadIdentity();
        gluPerspective(FIELD_OF_VIEW, ASPECT, Z_NEAR, Z_FAR);

        glMatrixMode(GL_MODELVIEW);
    }

    private void orthographicMode() {
        glMatrixMode(GL_PROJECTION);

        glLoadIdentity();
        glOrtho(-DISPLAY_WIDTH / 100.0f,
                DISPLAY_WIDTH / 100.0f,
                -DISPLAY_HEIGHT / 100.0f,
                DISPLAY_HEIGHT / 100.0f,
                Z_NEAR,
                Z_FAR);

        glMatrixMode(GL_MODELVIEW);
    }
}
