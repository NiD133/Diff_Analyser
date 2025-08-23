package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains unit tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that calling clear() on an instance with a null entity collection
     * does not throw a NullPointerException and correctly resets its state.
     * This is a regression test for a potential null-check issue.
     */
    @Test
    public void clear_whenEntityCollectionIsNull_shouldNotThrowException() {
        // Arrange: Create a ChartRenderingInfo instance with a null entity collection.
        // The constructor is designed to handle this scenario.
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo(null);

        // Act: Call the clear() method. The primary purpose of this test is to
        // ensure this action completes without throwing an exception.
        renderingInfo.clear();

        // Assert: Confirm that the object's state was properly cleared. This adds
        // robustness by verifying the method's behavior beyond just the absence of an error.
        assertTrue("The chart area should be empty after being cleared.",
                renderingInfo.getChartArea().isEmpty());
    }
}