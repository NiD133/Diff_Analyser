package org.jfree.data.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XYInterval} class, focusing on serialization.
 */
@DisplayName("XYInterval Serialization")
class XYIntervalTest {

    /**
     * This test verifies that an XYInterval instance can be serialized and
     * then deserialized back into an equal object, ensuring that its state is fully preserved.
     */
    @Test
    @DisplayName("An XYInterval instance should be serializable")
    void xyIntervalShouldBeSerializable() {
        // Arrange: Create an instance of XYInterval with clearly named parameters.
        double xLow = 1.0;
        double xHigh = 2.0;
        double y = 3.0;
        double yLow = 2.5;
        double yHigh = 3.5;
        XYInterval originalInterval = new XYInterval(xLow, xHigh, y, yLow, yHigh);

        // Act: Serialize the original object and then deserialize it into a new object.
        XYInterval deserializedInterval = TestUtils.serialised(originalInterval);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalInterval, deserializedInterval, "The deserialized interval should be equal to the original.");
    }
}