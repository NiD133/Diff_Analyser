package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the serialization of the {@link ShortTextTitle} class.
 */
@DisplayName("ShortTextTitle Serialization")
class ShortTextTitleTest {

    @Test
    @DisplayName("An instance should be equal to itself after serialization and deserialization")
    void objectIsEqualToItselfAfterSerialization() {
        // Arrange: Create an original instance of ShortTextTitle.
        ShortTextTitle originalTitle = new ShortTextTitle("Test Title");

        // Act: Serialize the original instance and then deserialize it back into a new object.
        ShortTextTitle deserializedTitle = TestUtils.serialised(originalTitle);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalTitle, deserializedTitle, "Deserialized title should be equal to the original.");
    }
}