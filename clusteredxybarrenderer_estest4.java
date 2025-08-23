package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClusteredXYBarRenderer_ESTestTest4 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Tests that findDomainBoundsWithOffset correctly calculates the domain range
     * for a dataset containing a single interval (bin). The expected behavior is
     * that the resulting range is centered on the interval's start value, with a
     * width equal to the interval's original width.
     */
    @Test
    public void findDomainBoundsWithOffset_withSingleBin_shouldCenterRangeOnBinStartValue() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Create a dataset with a single bin representing an interval from 1.0 to 3.0.
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series");
        final double binStart = 1.0;
        final double binEnd = 3.0;
        dataset.addBin(new SimpleHistogramBin(binStart, binEnd));

        // The method is expected to calculate a range centered on the bin's start value,
        // with a width equal to the bin's original width.
        // Original width = 3.0 - 1.0 = 2.0
        // New center     = 1.0 (the bin's start value)
        // New lower bound= 1.0 - (2.0 / 2) = 0.0
        // New upper bound= 1.0 + (2.0 / 2) = 2.0
        Range expectedRange = new Range(0.0, 2.0);

        // Act
        // Note: findDomainBoundsWithOffset() is a protected method.
        // This test can call it directly because it resides in the same package.
        Range actualRange = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        assertNotNull("The calculated range should not be null.", actualRange);
        assertEquals("The calculated domain range is incorrect.", expectedRange, actualRange);
    }
}