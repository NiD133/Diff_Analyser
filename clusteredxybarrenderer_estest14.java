package org.jfree.chart.renderer.xy;

import org.jfree.data.xy.DefaultWindDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

/**
 * Unit tests for the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that findDomainBounds() throws a ClassCastException when the provided
     * dataset does not implement the IntervalXYDataset interface. The method's
     * implementation relies on this specific dataset type for its calculations.
     */
    @Test(expected = ClassCastException.class)
    public void findDomainBounds_withNonIntervalDataset_throwsClassCastException() {
        // Arrange: Create a renderer and a dataset that is not an IntervalXYDataset.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset nonIntervalDataset = new DefaultWindDataset();

        // Act & Assert: Calling the method with the incompatible dataset type
        // is expected to cause a ClassCastException.
        renderer.findDomainBounds(nonIntervalDataset);
    }
}