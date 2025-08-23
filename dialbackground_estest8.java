package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An instance of DialBackground should always be equal to itself.
     */
    @Test
    public void equals_onSameInstance_shouldReturnTrue() {
        // Arrange
        DialBackground dialBackground = new DialBackground();

        // Act & Assert
        // An object must be equal to itself. This is the reflexive property of equality.
        assertEquals(dialBackground, dialBackground);
    }
}