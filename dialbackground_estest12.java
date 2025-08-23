package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the clone() method creates a new instance that is a deep
     * copy of the original. A correct clone should be logically equal to the
     * original but reside at a different memory address.
     */
    @Test
    public void clone_shouldReturnIndependentButEqualInstance() throws CloneNotSupportedException {
        // Arrange: Create an instance of DialBackground.
        DialBackground original = new DialBackground();

        // Act: Clone the original instance.
        DialBackground clone = (DialBackground) original.clone();

        // Assert: Verify the properties of the clone.
        // 1. The clone must not be the same instance as the original.
        assertNotSame("The cloned object should be a new instance.", original, clone);

        // 2. The clone must be logically equal to the original.
        assertEquals("The cloned object should be equal to the original.", original, clone);
    }
}