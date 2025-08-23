package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link ChartRenderingInfo} class, focusing on the clone() method.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that cloning a ChartRenderingInfo object with a null entity collection
     * results in a new, distinct object that is still logically equal to the original.
     */
    @Test
    public void clone_withNullEntityCollection_createsEqualAndDistinctObject() throws CloneNotSupportedException {
        // Arrange: Create a ChartRenderingInfo instance and set its entity collection to null.
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.setEntityCollection(null);

        // Act: Clone the original object.
        ChartRenderingInfo clonedInfo = (ChartRenderingInfo) originalInfo.clone();

        // Assert: The cloned object should be a different instance but have the same state.
        assertNotSame("The cloned object should be a new instance.", originalInfo, clonedInfo);
        assertEquals("The cloned object should be logically equal to the original.", originalInfo, clonedInfo);
        assertNull("The entity collection in the clone should also be null.", clonedInfo.getEntityCollection());
    }
}