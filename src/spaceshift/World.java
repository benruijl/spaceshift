package spaceshift;

public class World {
    private float shiftFactor;
    /* Some test objects */
    private final Box box;

    public World() {
        shiftFactor = (float) (0.5f * Math.PI);
        box = new Box();
    }

    public void draw() {
        box.draw();
    }

    public void update(int delta) {
        box.shift(shiftFactor);
    }

    public void doAction() {
        shiftFactor -= 0.01f;

        if (shiftFactor < 0) {
            shiftFactor = (float) (0.5f * Math.PI);
        }
    }

}
