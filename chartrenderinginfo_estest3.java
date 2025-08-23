package org.jfree.chart;

import org.jfree.chart.entity.EntityCollection;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that the entity collection can be set to null, and the
     * corresponding getter will subsequently return null.
     * <p>
     * This behavior is a documented feature, allowing developers to disable
     * entity collection to improve performance when it's not needed.
     * </p>
     */
    @Test
    public void testSetAndGetEntityCollectionWithNull() {
        // Arrange: Create a ChartRenderingInfo instance. By default, it has a
        // non-null entity collection.
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        assertNotNull("Precondition: EntityCollection should be non-null after default construction.",
                renderingInfo.getEntityCollection());

        // Act: Set the entity collection to null.
        renderingInfo.setEntityCollection(null);

        // Assert: Verify that the getter now returns null.
        EntityCollection retrievedCollection = renderingInfo.getEntityCollection();
        assertNull("The entity collection should be null after being explicitly set to null.",
                retrievedCollection);
    }
}