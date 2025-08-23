package org.jfree.chart.plot.dial;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the isClippedToWindow() method always returns true.
     * The background layer is designed to be consistently clipped within the dial's
     * visible window.
     */
    @Test
    public void isClippedToWindow_ShouldAlwaysReturnTrue() {
        // Arrange: Create a standard DialBackground instance.
        DialBackground dialBackground = new DialBackground();

        // Act & Assert: The method should consistently return true.
        assertTrue("The isClippedToWindow() method is expected to always return true.",
                   dialBackground.isClippedToWindow());
    }
}