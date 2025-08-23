package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Serialization tests for the {@link QuarterDateFormat} class.
 */
@DisplayName("QuarterDateFormat Serialization")
class QuarterDateFormatSerializationTest {

    /**
     * Verifies that a QuarterDateFormat instance remains equal to the original
     * after serialization and subsequent deserialization.
     */
    @Test
    @DisplayName("An object should be equal to its deserialized copy")
    void objectShouldBeEqualAfterSerializationAndDeserialization() {
        // Arrange: Create a formatter with a specific timezone and quarter symbols.
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        QuarterDateFormat originalFormatter = new QuarterDateFormat(gmt, QuarterDateFormat.REGULAR_QUARTERS);

        // Act: Serialize the original object and then deserialize it back.
        QuarterDateFormat deserializedFormatter = (QuarterDateFormat) TestUtils.serialised(originalFormatter);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalFormatter, deserializedFormatter, "The deserialized object should be equal to the original.");
    }
}