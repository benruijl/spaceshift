package spaceshift;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {
    private static final int FPS_CAP = 60;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final boolean DO_FULLSCREEN = false;

    /** time at last frame */
    private long lastFrame;

    private void prepareRender() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
    }

    public void start() {
        init();
        Keyboard.enableRepeatEvents(true);

        boolean terminate = false;

        World world = new World();

        while (!terminate && !Display.isCloseRequested()) {
            /* Check input */
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                terminate = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                world.doAction();
            }

            int delta = getDelta();
            world.update(delta);

            prepareRender();
            world.draw();
            Display.update();
            Display.sync(FPS_CAP);
        }

        Display.destroy();
    }

    /**
     * Calculate how many milliseconds have passed since last frame.
     * 
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    /**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void init() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setFullscreen(DO_FULLSCREEN);
            Display.setVSyncEnabled(true);
            Display.setTitle("Spaceshift");
            Display.create();
        } catch (Exception e) {
            System.out.println("Error setting up display");
            System.exit(0);
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, ((float) WIDTH / (float) HEIGHT), 0.1f,
                100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    public static void main(String[] args) {
        new Main().start();
    }

}
