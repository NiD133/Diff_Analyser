package org.jfree.chart.plot.dial;

import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.Test;

/**
 * Unit tests for the {@link DialBackground} class, focusing on exception handling.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the setGradientPaintTransformer() method throws an
     * IllegalArgumentException when a null argument is provided. The method's
     * contract requires a non-null transformer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setGradientPaintTransformer_withNullArgument_throwsIllegalArgumentException() {
        // Arrange: Create a new DialBackground instance.
        DialBackground dialBackground = new DialBackground();

        // Act: Attempt to set a null GradientPaintTransformer.
        // The @Test(expected=...) annotation will assert that an
        // IllegalArgumentException is thrown.
        dialBackground.setGradientPaintTransformer(null);
    }
}