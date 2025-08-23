package org.jfree.chart;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that getEntityCollection() returns the same collection instance
     * that was provided to the constructor.
     */
    @Test
    public void getEntityCollectionShouldReturnCollectionProvidedInConstructor() {
        // Arrange: Create an entity collection and a ChartRenderingInfo instance with it.
        EntityCollection expectedCollection = new StandardEntityCollection();
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo(expectedCollection);

        // Act: Retrieve the entity collection from the ChartRenderingInfo instance.
        EntityCollection actualCollection = renderingInfo.getEntityCollection();

        // Assert: The retrieved collection should be the same instance as the one provided.
        assertSame("The entity collection should be the same instance provided to the constructor.",
                expectedCollection, actualCollection);
    }
}