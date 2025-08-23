package org.jfree.chart.renderer.xy;

import org.jfree.data.xy.IntervalXYDataset;
import org.junit.Test;

/**
 * Unit tests for the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that calling findDomainBoundsWithOffset() with a null dataset
     * throws an IllegalArgumentException. This is the expected behavior for
     * methods that require a non-null dataset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void findDomainBoundsWithOffset_withNullDataset_shouldThrowException() {
        // Given
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // When
        renderer.findDomainBoundsWithOffset((IntervalXYDataset) null);

        // Then: an IllegalArgumentException is expected, as declared by the @Test annotation.
    }
}