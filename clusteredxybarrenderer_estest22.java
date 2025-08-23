package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link ClusteredXYBarRenderer} class, focusing on the
 * findDomainBoundsWithOffset method.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that findDomainBoundsWithOffset returns null when the provided
     * dataset is empty. An empty dataset has no data points, so there are no
     * domain bounds to calculate.
     */
    @Test
    public void findDomainBoundsWithOffset_shouldReturnNull_forEmptyDataset() {
        // Arrange: Create a renderer and an empty dataset.
        // A SimpleHistogramDataset is empty upon creation.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<String> emptyDataset = new SimpleHistogramDataset<>("Test Series Key");

        // Act: Calculate the domain bounds for the empty dataset.
        Range domainBounds = renderer.findDomainBoundsWithOffset(emptyDataset);

        // Assert: The resulting range should be null.
        assertNull("The domain bounds for an empty dataset should be null.", domainBounds);
    }
}