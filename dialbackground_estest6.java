package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;
import java.awt.Color;
import java.awt.Paint;

/**
 * Unit tests for the equals() method in the {@link DialBackground} class.
 */
public class DialBackgroundEqualsTest {

    /**
     * Verifies that two DialBackground instances are not considered equal if they
     * are created with different background paints.
     */
    @Test
    public void equals_shouldReturnFalse_whenPaintsAreDifferent() {
        // Arrange: Create two DialBackground instances with distinctly different paints.
        // The default constructor uses Color.WHITE.
        DialBackground backgroundWithDefaultPaint = new DialBackground();
        DialBackground backgroundWithCustomPaint = new DialBackground(Color.BLACK);

        // Act & Assert: The two instances should not be equal.
        assertNotEquals(backgroundWithDefaultPaint, backgroundWithCustomPaint);
    }
}