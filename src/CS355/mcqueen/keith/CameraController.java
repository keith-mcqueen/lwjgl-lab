package CS355.mcqueen.keith;

import CS355.LWJGL.Point3D;
import CS355.LWJGL.StudentLWJGLController;

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

    public static final float WALK_DISTANCE = 1.0f;
    public static final float ROTATION_ANGLE = 1.0f;
    public static final double _90_DEGREES = 90;

    // position of the camera (really the world)
    private float x = INITIAL_X;
    private float y = INITIAL_Y;
    private float z = INITIAL_Z;

    // orientation of the camera (the world, really) about the Y axis in radians
    private float yaw = 0.0f;

    private final GridModel gridModel = new GridModel(-100, 100, 10);
    private Model3D objFileModel;

    public CameraController(String[] args) {
        if (args.length > 0) {
            this.objFileModel = new ObjFileModel(args[0]);
        }
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
            this.x = INITIAL_X;
            this.y = INITIAL_Y;
            this.z = INITIAL_Z;
            this.yaw = 0.0f;

        }
    }

    @Override
    public void render() {
        // super calls glClear() for us
        super.render();

        // update the camera position
        this.lookThrough();

        // render the "floor" grid
//        Renderer renderer = new WireframeRenderer();
//        renderer.setColor(0.0f, 1.0f, 0.0f);
//        this.gridModel.render(renderer);

        // render the obj-file model if there is one
        if (null != this.objFileModel) {
            // ambient light source
            AmbientLightSource a = new AmbientLightSource(new Color(0.4, 0.4, 0.4));

            // directional light source
            DirectionalLightSource d = new DirectionalLightSource(new Color(0.4, 0.4, 0.4), new Point3D(1.0, 1.0, 1.0));

            // point light source(s)
            PointLightSource p1 = new PointLightSource(new Color(6.0, 0.0, 0.0), new Point3D(80.0, 80.0, 80.0));
            PointLightSource p2 = new PointLightSource(new Color(0.0, 6.0, 0.0), new Point3D(-80.0, -80.0, -80.0));
            PointLightSource p3 = new PointLightSource(new Color(0.0, 0.0, 6.0), new Point3D(0.0, 80.0, 80.0));

            // shader
            FlatShader shader = new FlatShader(a, d, p1, p2, p3);

            // renderer
            Renderer renderer = new SurfaceRenderer(shader);
            renderer.setColor(1.0f, 1.0f, 1.0f);
            this.objFileModel.render(renderer);
        }
    }

    private void lookThrough() {
        // "reset" all transformations
        glLoadIdentity();

        // translate
        glTranslatef(this.x, this.y, this.z);

        // rotate about the y axis
        glRotatef(this.yaw, 0.0f, 1.0f, 0.0f);
    }

    private void forward(float distance) {
        this.x -= distance * (float) sin(toRadians(this.yaw));
        this.z += distance * (float) cos(toRadians(this.yaw));
    }

    private void back(float distance) {
        this.x += distance * (float) sin(toRadians(this.yaw));
        this.z -= distance * (float) cos(toRadians(this.yaw));
    }

    private void left(float distance) {
        this.x -= distance * (float) sin(toRadians(this.yaw - _90_DEGREES));
        this.z += distance * (float) cos(toRadians(this.yaw - _90_DEGREES));
    }

    private void right(float distance) {
        this.x -= distance * (float) sin(toRadians(this.yaw + _90_DEGREES));
        this.z += distance * (float) cos(toRadians(this.yaw + _90_DEGREES));
    }

    private void fly(float distance) {
        this.y += distance;
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
