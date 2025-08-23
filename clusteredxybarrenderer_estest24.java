package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * This test focuses on the behavior of the {@link ClusteredXYBarRenderer#findDomainBounds(XYDataset)} method.
 * Note: The class name and its parent are preserved from the original auto-generated test suite.
 */
public class ClusteredXYBarRenderer_ESTestTest24 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Verifies that findDomainBounds() returns null when provided with a null dataset.
     * This is the expected behavior, as domain bounds cannot be determined without data.
     */
    @Test(timeout = 4000)
    public void findDomainBounds_shouldReturnNull_whenDatasetIsNull() {
        // Arrange: Create an instance of the renderer.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act: Call the method under test with a null dataset.
        Range domainBounds = renderer.findDomainBounds(null);

        // Assert: The method should return null, indicating no bounds could be found.
        assertNull("The domain bounds should be null for a null dataset.", domainBounds);
    }
}