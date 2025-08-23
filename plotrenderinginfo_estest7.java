package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getOwner() returns null when the PlotRenderingInfo
     * is constructed with a null owner.
     */
    @Test
    public void getOwnerShouldReturnNullWhenConstructedWithNullOwner() {
        // Arrange: Create a PlotRenderingInfo instance with a null owner.
        PlotRenderingInfo plotRenderingInfo = new PlotRenderingInfo(null);

        // Act: Retrieve the owner from the instance.
        ChartRenderingInfo owner = plotRenderingInfo.getOwner();

        // Assert: The retrieved owner should be null.
        assertNull("The owner should be null as it was set to null in the constructor.", owner);
    }
}