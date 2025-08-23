package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class, focusing on domain bounds calculation.
 */
public class ClusteredXYBarRenderer_ESTestTest6 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Verifies that findDomainBoundsWithOffset() correctly calculates a domain range
     * for a dataset containing a single interval (a histogram bin).
     * The resulting range should be centered around the start value of the interval.
     */
    @Test
    public void findDomainBoundsWithOffset_forSingleBinDataset_shouldCenterRangeOnBinStartValue() {
        // Arrange
        final double binStart = 1200.0;
        final double binEnd = 1300.0;
        final double binWidth = binEnd - binStart; // 100.0

        // The method under test creates a range centered on the bin's start value.
        final double expectedCenter = binStart;
        final double expectedLowerBound = binStart - (binWidth / 2.0); // 1200.0 - 50.0 = 1150.0
        final double expectedUpperBound = binStart + (binWidth / 2.0); // 1200.0 + 50.0 = 1250.0

        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Create a dataset with one series containing a single bin.
        SimpleHistogramBin bin = new SimpleHistogramBin(binStart, binEnd);
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series");
        dataset.addBin(bin);

        // Act
        Range resultRange = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        // The core logic is that the range is centered on the interval's start value.
        assertEquals("The central value of the range should be the bin's start value.",
                expectedCenter, resultRange.getCentralValue(), 0.001);

        // We also verify the bounds for completeness.
        assertEquals("The lower bound of the range is incorrect.",
                expectedLowerBound, resultRange.getLowerBound(), 0.001);
        assertEquals("The upper bound of the range is incorrect.",
                expectedUpperBound, resultRange.getUpperBound(), 0.001);
    }
}