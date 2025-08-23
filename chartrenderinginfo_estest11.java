package org.jfree.chart;

import org.jfree.chart.entity.EntityCollection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite focuses on the equals() method of the ChartRenderingInfo class.
 */
// The original class name and inheritance are preserved as they may be part of a larger test setup.
public class ChartRenderingInfo_ESTestTest11 extends ChartRenderingInfo_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false when comparing two
     * ChartRenderingInfo objects with different entity collections.
     */
    @Test
    public void equals_shouldReturnFalse_whenEntityCollectionsDiffer() {
        // Arrange: Create two ChartRenderingInfo objects. By default, they are
        // constructed with a non-null StandardEntityCollection and are equal.
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();

        // Sanity check: Ensure the two new instances are equal initially.
        assertEquals("Two newly created ChartRenderingInfo instances should be equal.", info1, info2);

        // Act: Modify the entity collection of the second object to be null.
        info2.setEntityCollection(null);

        // Assert: The two objects should no longer be equal.
        assertNotEquals("Instances should not be equal after one's entity collection is set to null.", info1, info2);
    }
}