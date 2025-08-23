/**
 * A "long" needle for a meter plot. This is a subclass of MeterNeedle
 * that overrides the default rotation point.
 */
public class LongNeedle extends MeterNeedle {
    /**
     * Default constructor. Sets the rotation point Y-coordinate to 0.8.
     */
    public LongNeedle() {
        super();
        setRotateY(0.8);
    }

    /**
     * Draws the needle. This method's implementation is not relevant for the
     * test being improved, so it is left empty.
     */
    @Override
    protected void drawNeedle(Graphics2D g2, java.awt.geom.Rectangle2D plotArea,
                              java.awt.geom.Point2D rotate, double angle) {
        // Implementation not required for this test.
    }
}