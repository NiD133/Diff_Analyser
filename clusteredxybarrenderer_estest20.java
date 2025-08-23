package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * A test suite for the equals() method in the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererEqualsTest {

    /**
     * Verifies that the equals() method returns false when a ClusteredXYBarRenderer
     * instance is compared to an object of a different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentObjectType() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        Object otherObject = new Object();

        // Act
        boolean isEqual = renderer.equals(otherObject);

        // Assert
        assertFalse("A renderer instance should not be equal to an object of a completely different type.", isEqual);
    }
}