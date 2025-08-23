package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that two newly created, empty StandardEntityCollection instances
     * are considered equal.
     */
    @Test
    public void equals_onTwoEmptyCollections_shouldReturnTrue() {
        // Arrange: Create two separate, empty entity collections.
        StandardEntityCollection collection1 = new StandardEntityCollection();
        StandardEntityCollection collection2 = new StandardEntityCollection();

        // Act & Assert: Verify that the two empty collections are equal.
        // The assertEquals method is the standard way to check for object equality
        // in tests and provides a more informative failure message than assertTrue.
        assertEquals(collection1, collection2);
    }
}