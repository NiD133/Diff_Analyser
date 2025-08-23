package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;

/**
 * Unit tests for the equals() method in the MeterNeedle class.
 */
public class MeterNeedleTest {

    /**
     * Verifies that two MeterNeedle instances are not considered equal if one
     * has a different outline paint.
     */
    @Test
    public void testEquals_differentOutlinePaint_returnsFalse() {
        // Arrange: Create two needle instances. By default, their outline paint is black.
        PointerNeedle needle1 = new PointerNeedle();
        PointerNeedle needle2 = new PointerNeedle();

        // Act: Change the outline paint on the second needle.
        needle2.setOutlinePaint(Color.MAGENTA);

        // Assert: The two needles should no longer be equal.
        assertFalse("Needles with different outline paints should not be equal.", needle1.equals(needle2));
    }
}