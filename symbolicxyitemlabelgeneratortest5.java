package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on serialization.
 */
class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that a default instance of SymbolicXYItemLabelGenerator can be serialized
     * and then deserialized, resulting in an object that is equal to the original.
     * This ensures that the object's state is correctly preserved across serialization.
     */
    @Test
    @DisplayName("A default generator should be correctly restored after serialization")
    void serialization_ofDefaultInstance_restoresEqualObject() {
        // Arrange: Create a new generator instance.
        var originalGenerator = new SymbolicXYItemLabelGenerator();

        // Act: Serialize and then deserialize the instance.
        var deserializedGenerator = TestUtils.serialised(originalGenerator);

        // Assert: The deserialized instance should be equal to the original.
        assertEquals(originalGenerator, deserializedGenerator,
                "The deserialized generator should be equal to the original.");
    }
}