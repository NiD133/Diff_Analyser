package org.jfree.chart.labels;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
class IntervalCategoryToolTipGeneratorTest {

    /**
     * This test verifies that an instance of IntervalCategoryToolTipGenerator
     * can be serialized and then deserialized without losing its state. The
     * deserialized object must be equal to the original, confirming that the
     * equals() and hashCode() methods are consistent with the serialization logic.
     */
    @Test
    @DisplayName("A generator, when serialized and deserialized, should be equal to the original")
    void serializationShouldPreserveObjectEquality() {
        // Arrange: Create a generator with a non-default label format and date formatter
        // to ensure its state is properly saved and restored.
        String labelFormat = "{3} - {4}";
        DateFormat dateFormat = DateFormat.getInstance();
        IntervalCategoryToolTipGenerator originalGenerator = new IntervalCategoryToolTipGenerator(labelFormat, dateFormat);

        // Act: Serialize the original object and then deserialize it into a new instance.
        IntervalCategoryToolTipGenerator deserializedGenerator = 
                (IntervalCategoryToolTipGenerator) TestUtils.serialised(originalGenerator);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalGenerator, deserializedGenerator, 
                "The deserialized generator should be equal to the original.");
    }
}