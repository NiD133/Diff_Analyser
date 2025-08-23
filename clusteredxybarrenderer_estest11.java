package org.jfree.chart.renderer.xy;

import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * This test class is a refactored version of an auto-generated test.
 * It focuses on a specific scenario for the ClusteredXYBarRenderer class.
 */
public class ClusteredXYBarRenderer_ESTestTest11 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Tests that findDomainBoundsWithOffset correctly propagates a NullPointerException
     * when the provided dataset is in an invalid state (contains a null bin).
     *
     * The renderer itself does not perform the calculation but delegates to the dataset.
     * This test ensures that exceptions from the dataset are not swallowed.
     */
    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_whenDatasetContainsNullBin_thenThrowsNullPointerException() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        // Create a dataset that will be put into an invalid state.
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series Key");

        // Intentionally add a null bin to the dataset to trigger an error during data processing.
        dataset.addBin((SimpleHistogramBin) null);

        // Act & Assert
        try {
            // This call is expected to fail because the dataset cannot process the null bin.
            renderer.findDomainBoundsWithOffset(dataset);
            fail("A NullPointerException was expected but was not thrown.");
        } catch (NullPointerException e) {
            // This exception is expected. It confirms that the renderer correctly
            // propagates the error from the underlying dataset.
        }
    }
}