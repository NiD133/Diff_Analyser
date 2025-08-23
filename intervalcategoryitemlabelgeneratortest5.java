package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that an instance of IntervalCategoryItemLabelGenerator remains equal
     * to the original after being serialized and deserialized. This ensures that
     * the object's state is correctly preserved.
     */
    @Test
    @DisplayName("Serialization should preserve object equality")
    void serializationShouldPreserveObjectState() {
        // Arrange: Create an instance of the label generator with a specific
        // format and a DateFormat instance.
        String labelFormat = "{3} - {4}";
        DateFormat dateFormat = DateFormat.getInstance();
        IntervalCategoryItemLabelGenerator originalGenerator = new IntervalCategoryItemLabelGenerator(labelFormat, dateFormat);

        // Act: Serialize and then deserialize the generator instance.
        IntervalCategoryItemLabelGenerator deserializedGenerator = TestUtils.serialised(originalGenerator);

        // Assert: The deserialized generator must be equal to the original.
        assertEquals(originalGenerator, deserializedGenerator);
    }
}