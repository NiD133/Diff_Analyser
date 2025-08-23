package org.jfree.chart.entity;

import org.junit.Test;
import java.util.Iterator;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that the iterator() method returns a non-null iterator
     * when the collection is empty. This ensures that clients can safely
     * iterate over any collection without a null check.
     */
    @Test
    public void iterator_onEmptyCollection_shouldReturnNonNullIterator() {
        // Arrange: Create a new, empty entity collection.
        StandardEntityCollection emptyCollection = new StandardEntityCollection();

        // Act: Request an iterator from the empty collection.
        Iterator<ChartEntity> iterator = emptyCollection.iterator();

        // Assert: The returned iterator should not be null.
        assertNotNull("The iterator for an empty collection should never be null.", iterator);
    }
}