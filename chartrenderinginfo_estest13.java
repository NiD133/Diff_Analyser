package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method of the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that the equals() method returns false when a ChartRenderingInfo
     * object is compared to an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        Object unrelatedObject = new Object(); // An object of a different class

        // Act & Assert
        // The equals method must return false for objects of different types.
        assertFalse(info.equals(unrelatedObject));
    }
}