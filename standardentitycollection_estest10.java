package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingAnInstanceToItself() {
        // Arrange: Create an instance of the class under test.
        StandardEntityCollection collection = new StandardEntityCollection();

        // Act & Assert: An object should always be equal to itself.
        assertTrue("A collection instance should always be equal to itself.",
                collection.equals(collection));
    }
}