package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the serialization of the {@link PlotRenderingInfo} class.
 */
class PlotRenderingInfoTest {

    /**
     * Verifies that a PlotRenderingInfo object, after being serialized and
     * deserialized, remains equal to the original. This test includes non-default
     * states for plot areas and subplots to ensure custom serialization logic
     * for transient fields is working correctly.
     */
    @Test
    @DisplayName("Serialization should correctly restore a PlotRenderingInfo object")
    void serialization_ofInstance_shouldPreserveState() {
        // Arrange: Create a PlotRenderingInfo instance with custom data, including
        // a plot area, data area, and a nested subplot.
        var chartInfo = new ChartRenderingInfo();
        var originalInfo = new PlotRenderingInfo(chartInfo);
        originalInfo.setPlotArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        originalInfo.setDataArea(new Rectangle2D.Double(5.0, 6.0, 7.0, 8.0));

        var subplotInfo = new PlotRenderingInfo(chartInfo);
        subplotInfo.setPlotArea(new Rectangle2D.Double(11.0, 12.0, 13.0, 14.0));
        subplotInfo.setDataArea(new Rectangle2D.Double(15.0, 16.0, 17.0, 18.0));
        originalInfo.addSubplotInfo(subplotInfo);

        // Act: Serialize and then deserialize the instance.
        PlotRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);

        // Assert: The deserialized instance should be equal to the original.
        assertEquals(originalInfo, deserializedInfo);
    }
}