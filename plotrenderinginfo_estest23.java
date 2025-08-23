package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.StandardEntityCollection;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link PlotRenderingInfo} class, focusing on its equality contract.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that the equals() method returns false when a PlotRenderingInfo
     * object is compared to an object of an unrelated type. This is a standard
     * check for the equals() contract.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithUnrelatedType() {
        // Arrange: Create a PlotRenderingInfo instance and an instance of an unrelated class.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        Object unrelatedObject = new StandardEntityCollection();

        // Act: Compare the two objects for equality.
        boolean result = plotInfo.equals(unrelatedObject);

        // Assert: The result of the comparison should be false.
        assertFalse(result);
    }
}