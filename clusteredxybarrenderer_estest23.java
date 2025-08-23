package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that findDomainBounds() returns null when the dataset is empty.
     * An empty dataset has no data points, so there are no domain bounds to calculate.
     */
    @Test
    public void findDomainBounds_withEmptyDataset_shouldReturnNull() {
        // Arrange: Create a renderer and an empty dataset.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset emptyDataset = new CategoryTableXYDataset();

        // Act: Calculate the domain bounds for the empty dataset.
        Range domainBounds = renderer.findDomainBounds(emptyDataset);

        // Assert: The resulting range should be null.
        assertNull("The domain bounds for an empty dataset should be null.", domainBounds);
    }
}