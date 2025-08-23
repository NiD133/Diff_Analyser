package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Paint;

// The test class definition remains the same as the original.
public class MeterNeedle_ESTestTest32 extends MeterNeedle_ESTest_scaffolding {

    /**
     * Verifies that the fill paint of a MeterNeedle can be successfully set to null.
     */
    @Test
    public void setFillPaint_withNullValue_shouldUpdateFillPaintProperty() {
        // Arrange: Create a needle and set an initial non-null fill paint
        // to ensure we are testing a genuine state change.
        PointerNeedle needle = new PointerNeedle();
        needle.setFillPaint(Color.RED);

        // Act: Set the fill paint to null.
        needle.setFillPaint(null);

        // Assert: Verify the fill paint is now null.
        Paint result = needle.getFillPaint();
        assertNull("The fill paint should be null after being set to null.", result);
    }
}