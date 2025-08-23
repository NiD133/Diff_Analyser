package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the serialization of the {@link ModuloAxis} class.
 */
class ModuloAxisTest {

    /**
     * Verifies that a ModuloAxis instance, after being serialized and deserialized,
     * remains equal to the original instance.
     */
    @Test
    @DisplayName("A ModuloAxis instance should be equal to the original after serialization and deserialization")
    void serializationAndDeserialization_shouldPreserveObjectEquality() {
        // Arrange: Create a ModuloAxis instance with specific properties.
        String axisLabel = "Test Axis";
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis originalAxis = new ModuloAxis(axisLabel, fixedRange);

        // Act: Serialize and then deserialize the axis instance.
        ModuloAxis deserializedAxis = (ModuloAxis) TestUtils.serialised(originalAxis);

        // Assert: The deserialized object should be a new instance, but logically equal to the original.
        assertNotSame(originalAxis, deserializedAxis, "Serialization should create a new object instance.");
        assertEquals(originalAxis, deserializedAxis, "The deserialized axis should be equal to the original.");
    }
}