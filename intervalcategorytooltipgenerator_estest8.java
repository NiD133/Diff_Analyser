package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();

        // Act & Assert
        // According to the Java contract for equals(), an object must be equal to itself.
        assertTrue("An object should be equal to itself.", generator.equals(generator));
    }
}