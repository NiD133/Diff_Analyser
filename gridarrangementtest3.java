package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    /**
     * This test verifies that the state of a GridArrangement object is correctly
     * preserved after serialization and subsequent deserialization.
     */
    @Test
    @DisplayName("A serialized and deserialized GridArrangement is equal to the original")
    void serialization_shouldPreserveState() {
        // Arrange: Create a GridArrangement instance with specific dimensions.
        GridArrangement originalArrangement = new GridArrangement(33, 44);

        // Act: Serialize the instance and then deserialize it back into an object.
        GridArrangement deserializedArrangement = TestUtils.serialised(originalArrangement);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalArrangement, deserializedArrangement);
    }
}