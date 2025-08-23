package org.jfree.chart.labels;

import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This test class evaluates the SymbolicXYItemLabelGenerator, particularly its
 * behavior when interacting with datasets that are in an unusual or invalid state.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * This test verifies that if the underlying dataset throws a NullPointerException
     * during data access, the generator correctly propagates this exception.
     *
     * The test case reproduces a specific scenario where a DynamicTimeSeriesCollection
     * is initialized for a certain number of series, but data is only provided for a
     * subset of them. This inconsistent state is known to cause an NPE within the
     * dataset class itself, which the label generator is expected to not suppress.
     */
    @Test
    public void generateToolTip_whenDatasetThrowsNPE_shouldPropagateException() {
        // ARRANGE: Create a generator and a dataset in an inconsistent state.
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // 1. Configure a dataset to hold 6 series.
        final int totalSeriesCount = 6;
        final int maxItemCount = 6;
        DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(
                totalSeriesCount, maxItemCount);

        // 2. Provide data for only 5 of the 6 series. This mismatch is the key
        //    to triggering the internal NullPointerException in the dataset.
        final int populatedSeriesCount = 5;
        float[] incompleteDataForSeries = new float[populatedSeriesCount];
        int itemIndex = 2;
        int seriesIndex = 0; // The 'series' parameter for this specific appendData method.
        dataset.appendData(incompleteDataForSeries, itemIndex, seriesIndex);

        // 3. Define the coordinates to query, which fall within the populated data range.
        int seriesToQuery = 1;
        int itemToQuery = 2;

        // ACT & ASSERT: Expect an NPE originating from the dataset.
        try {
            generator.generateToolTip(dataset, seriesToQuery, itemToQuery);
            fail("A NullPointerException was expected but was not thrown.");
        } catch (NullPointerException e) {
            // Verify that the exception originates from the dataset class, not the generator.
            // This confirms the generator is simply propagating an issue from its data source.
            String originatingClassName = e.getStackTrace()[0].getClassName();
            assertEquals("Exception should originate from DynamicTimeSeriesCollection",
                    "org.jfree.data.time.DynamicTimeSeriesCollection",
                    originatingClassName);

            // The original test also confirmed the exception has no message.
            assertNull("The exception message was expected to be null", e.getMessage());
        }
    }
}