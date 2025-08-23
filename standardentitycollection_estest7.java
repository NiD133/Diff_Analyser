package org.jfree.chart.entity;

import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that getEntities() on a newly instantiated collection
     * returns a non-null, empty collection.
     */
    @Test
    public void getEntities_onNewCollection_returnsEmptyCollection() {
        // Arrange: Create a new, empty entity collection.
        StandardEntityCollection entityCollection = new StandardEntityCollection();

        // Act: Retrieve the collection of entities.
        Collection<ChartEntity> entities = entityCollection.getEntities();

        // Assert: The returned collection should exist and be empty.
        assertNotNull("The returned collection should never be null.", entities);
        assertTrue("A new StandardEntityCollection should have no entities.", entities.isEmpty());
    }
}