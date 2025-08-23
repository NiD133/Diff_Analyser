package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that the equals() method returns false when a StandardEntityCollection
     * is compared to an object of a different, incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        StandardEntityCollection collection = new StandardEntityCollection();
        Object nonCollectionObject = new Object();

        // Act & Assert
        assertFalse("A collection should not be equal to an object of a different type.",
                collection.equals(nonCollectionObject));
    }
}