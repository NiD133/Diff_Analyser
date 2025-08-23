package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A collection of tests for the {@link ClusteredXYBarRenderer#findDomainBounds(XYDataset)} method.
 */
public class ClusteredXYBarRendererDomainBoundsTest {

    /**
     * Verifies that findDomainBounds() correctly calculates the range for a dataset
     * that contains a single histogram bin. The expected range should match the
     * boundaries of that single bin.
     */
    @Test
    public void findDomainBounds_withSingleBinHistogram_returnsRangeOfBin() {
        // Arrange: Create a renderer and a dataset with one bin from -10.0 to 0.0
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series");

        final double binLowerBound = -10.0;
        final double binUpperBound = 0.0;
        dataset.addBin(new SimpleHistogramBin(binLowerBound, binUpperBound));

        // Act: Calculate the domain bounds from the dataset
        Range domainBounds = renderer.findDomainBounds(dataset);

        // Assert: The calculated bounds should match the bin's boundaries
        assertNotNull("The domain bounds should not be null.", domainBounds);
        assertEquals("The lower bound should match the bin's lower bound.", 
                     binLowerBound, domainBounds.getLowerBound(), 0.0);
        assertEquals("The upper bound should match the bin's upper bound.", 
                     binUpperBound, domainBounds.getUpperBound(), 0.0);
    }
}