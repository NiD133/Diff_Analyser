package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.BasicStroke;

/**
 * This test class focuses on the equality logic of MeterNeedle implementations.
 * Note: The original test class name "MeterNeedle_ESTestTest26" is an artifact
 * of a test generation tool. In a typical project, this test would be part of a
 * more descriptively named class like "MiddlePinNeedleTest".
 */
public class MeterNeedle_ESTestTest26 {

    /**
     * Verifies that the equals() method returns false for two MiddlePinNeedle
     * objects that differ only by their outline stroke.
     */
    @Test
    public void equals_shouldReturnFalse_whenOutlineStrokesDiffer() {
        // Arrange: Create a needle and a clone of it.
        MiddlePinNeedle originalNeedle = new MiddlePinNeedle();
        MiddlePinNeedle clonedNeedle = (MiddlePinNeedle) originalNeedle.clone();

        // Sanity check: Ensure the clone is equal to the original initially.
        assertEquals("A fresh clone should be equal to its original.", originalNeedle, clonedNeedle);

        // Act: Modify the outline stroke of the cloned needle to be different.
        BasicStroke differentStroke = new BasicStroke(1.0f); // Default is 2.0f
        clonedNeedle.setOutlineStroke(differentStroke);

        // Assert: The needles should no longer be considered equal.
        assertNotEquals("Needles with different outline strokes should not be equal.", originalNeedle, clonedNeedle);
    }
}