package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the equals() method in the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * According to the equals() contract, an object must be equal to itself.
     */
    @Test
    public void testEqualsIsReflexive() {
        // Arrange: Create an instance of the class under test.
        ChartRenderingInfo info = new ChartRenderingInfo();

        // Act & Assert: An object must always be equal to itself.
        assertTrue("An instance of ChartRenderingInfo should be equal to itself.", info.equals(info));
    }
}