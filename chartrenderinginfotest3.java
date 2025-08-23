package org.jfree.chart;

import org.junit.jupiter.api.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A collection of tests for the serialization of the {@link ChartRenderingInfo} class.
 */
class ChartRenderingInfoSerializationTest {

    /**
     * Verifies that a ChartRenderingInfo instance can be serialized and then
     * deserialized, resulting in an object that is equal to the original.
     * This is crucial for features like saving chart state or transferring
     * it over a network.
     */
    @Test
    void aSerializedAndDeserializedInstanceShouldBeEqualToTheOriginal() {
        // Arrange: Create an instance and configure its state.
        // The chartArea is a transient field with custom serialization logic,
        // so it's important to test its state is preserved.
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Act: Serialize and then deserialize the instance.
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalInfo, deserializedInfo);
    }
}