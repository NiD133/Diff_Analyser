package org.jfree.chart.plot.dial;

import org.junit.Test;
import java.awt.Paint;

/**
 * Contains unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the setPaint() method correctly throws an
     * IllegalArgumentException when a null argument is provided. The background
     * paint is a required component and cannot be null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setPaint_withNullArgument_shouldThrowIllegalArgumentException() {
        // Arrange: Create a new DialBackground instance.
        DialBackground dialBackground = new DialBackground();

        // Act: Attempt to set the paint property to null.
        // Assert: The @Test(expected) annotation asserts that an
        // IllegalArgumentException is thrown by this method call.
        dialBackground.setPaint((Paint) null);
    }
}