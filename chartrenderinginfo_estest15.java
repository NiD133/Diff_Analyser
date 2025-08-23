package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Contains tests for the {@link ChartRenderingInfo} class, focusing on its cloning capabilities.
 */
public class ChartRenderingInfoCloningTest {

    /**
     * Verifies that cloning a ChartRenderingInfo object creates a new instance
     * that is equal in value to the original. This test confirms adherence to the
     * general contract of Object.clone().
     */
    @Test
    public void clone_shouldProduceEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an original ChartRenderingInfo instance.
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();

        // Act: Create a clone of the original instance.
        // Note: The clone() method in ChartRenderingInfo returns an Object, so a cast is necessary.
        ChartRenderingInfo clonedInfo = (ChartRenderingInfo) originalInfo.clone();

        // Assert: The clone should be a different object in memory, but its contents should be equal.
        assertNotSame("A clone must not be the same instance as the original.", originalInfo, clonedInfo);
        assertEquals("A clone must be equal in value to the original.", originalInfo, clonedInfo);
    }
}