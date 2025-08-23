package org.jfree.chart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the serialization of the {@link ChartRenderingInfo} class.
 */
class ChartRenderingInfoSerializationTest {

    @Test
    @DisplayName("A deserialized ChartRenderingInfo object should be equal to the original")
    void serialization_restoresObjectStateAndInternalOwnerReference() {
        // Arrange: Create an instance and set a property to ensure it's not in a default state.
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.getPlotInfo().setDataArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Act: Serialize and then deserialize the object.
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);

        // Assert: Verify that the deserialized object is a faithful copy.

        // 1. The deserialized object should be equal to the original based on the equals() method.
        assertEquals(originalInfo, deserializedInfo,
                "Deserialized object should be equal to the original.");

        // 2. The internal 'owner' reference in PlotRenderingInfo must point to the new
        //    deserialized ChartRenderingInfo instance. This verifies that the object graph
        //    is correctly reconstructed after deserialization.
        assertSame(deserializedInfo, deserializedInfo.getPlotInfo().getOwner(),
                "The owner of the plot info should be the new deserialized instance.");
    }
}