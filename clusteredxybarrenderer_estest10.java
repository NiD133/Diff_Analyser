package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@code findDomainBounds} method in the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererFindDomainBoundsTest {

    /**
     * Verifies that findDomainBounds correctly calculates the domain range
     * for a SimpleHistogramDataset containing a single bin. The resulting
     * range should match the bin's boundaries.
     */
    @Test
    public void findDomainBounds_withSingleBinHistogram_returnsCorrectRange() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series");

        // A SimpleHistogramDataset is a type of IntervalXYDataset, which the
        // findDomainBounds method is designed to handle.
        double binStart = -10.0;
        double binEnd = 0.0;
        SimpleHistogramBin bin = new SimpleHistogramBin(binStart, binEnd);
        dataset.addBin(bin);

        // Act
        Range domainBounds = renderer.findDomainBounds(dataset);

        // Assert
        assertNotNull("The calculated domain bounds should not be null.", domainBounds);
        assertEquals("The lower bound of the range should match the bin's start value.",
                binStart, domainBounds.getLowerBound(), 0.0);
        assertEquals("The upper bound of the range should match the bin's end value.",
                binEnd, domainBounds.getUpperBound(), 0.0);
    }
}