package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test suite for the ClusteredXYBarRenderer class.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that findDomainBounds calculates the correct range for a dataset
     * where all data points share the same x-value (timestamp).
     * The resulting range should have its lower and upper bounds equal to that single timestamp.
     */
    @Test(timeout = 4000)
    public void findDomainBounds_whenAllDataItemsHaveSameXValue_returnsRangeWithIdenticalBounds() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // The original test used a complex mock setup to generate a date.
        // We use the resulting timestamp directly for clarity and reproducibility.
        // This timestamp corresponds to Fri, 14 Feb 2014 20:21:21.320 GMT.
        long singleTimestamp = 1392409281320L;
        Date sharedDate = new Date(singleTimestamp);

        // Create a dataset where multiple items share the same x-value (date).
        // The y-values (open, high, low, close) are irrelevant for the domain bounds.
        OHLCDataItem[] dataItems = new OHLCDataItem[]{
                new OHLCDataItem(sharedDate, 8.0, 10.0, 7.0, 9.0, 1000),
                new OHLCDataItem(sharedDate, 8.1, 10.1, 7.1, 9.1, 1100),
                new OHLCDataItem(sharedDate, 8.2, 10.2, 7.2, 9.2, 1200)
        };
        DefaultOHLCDataset dataset = new DefaultOHLCDataset("SERIES_1", dataItems);

        // Act
        Range domainRange = renderer.findDomainBounds(dataset);

        // Assert
        assertNotNull("The calculated domain range should not be null.", domainRange);

        // For a dataset with only one unique x-value, the lower and upper bounds of the range
        // should both be equal to that x-value.
        double expectedBound = singleTimestamp;
        assertEquals("Lower bound should match the single timestamp.",
                expectedBound, domainRange.getLowerBound(), 0.01);
        assertEquals("Upper bound should also match the single timestamp.",
                expectedBound, domainRange.getUpperBound(), 0.01);
    }
}