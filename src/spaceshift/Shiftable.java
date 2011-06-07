package spaceshift;

public interface Shiftable {
    void shift(float shiftFactor);
    void shrink(float shrinkFactor);

    boolean doShift();
}
