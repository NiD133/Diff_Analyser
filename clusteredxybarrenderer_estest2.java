package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the findDomainBoundsWithOffset method in the ClusteredXYBarRenderer class.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that findDomainBoundsWithOffset correctly calculates the domain range
     * for a dataset containing multiple bins, including one with negative values.
     *
     * The method is expected to calculate a new interval for each bin, centered on
     * the bin's start value, and then return the total union of these new intervals.
     */
    @Test
    public void findDomainBoundsWithOffset_withMixedIntervals_shouldCalculateCorrectRange() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series");

        // Add a bin with a positive interval [100.0, 120.0].
        // The offset is (120 - 100) / 2 = 10.
        // The adjusted interval for range calculation is [100 - 10, 100 + 10] -> [90.0, 110.0].
        dataset.addBin(new SimpleHistogramBin(100.0, 120.0, true, true));

        // Add a bin with a negative interval [-20.0, 0.0].
        // The offset is (0 - (-20)) / 2 = 10.
        // The adjusted interval for range calculation is [-20 - 10, -20 + 10] -> [-30.0, -10.0].
        dataset.addBin(new SimpleHistogramBin(-20.0, 0.0, true, true));

        // The expected total range is the union of [90.0, 110.0] and [-30.0, -10.0],
        // which results in the range [-30.0, 110.0].
        double expectedLowerBound = -30.0;
        double expectedUpperBound = 110.0;

        // Act
        Range domainBounds = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        assertNotNull("The calculated domain bounds should not be null.", domainBounds);
        assertEquals("The lower bound of the range is incorrect.",
                expectedLowerBound, domainBounds.getLowerBound(), 0.000001);
        assertEquals("The upper bound of the range is incorrect.",
                expectedUpperBound, domainBounds.getUpperBound(), 0.000001);
    }
}