package org.jfree.data;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the {@link ComparableObjectItem} class, focusing on its serialization.
 */
class ComparableObjectItemTest {

    /**
     * Verifies that an object remains equal to the original after serialization
     * and deserialization, but is not the same instance.
     */
    @Test
    @DisplayName("A serialized and deserialized instance should be a new, equal object")
    void serialization_shouldPreserveEquality() {
        // Arrange
        var originalItem = new ComparableObjectItem(1, "XYZ");

        // Act
        var deserializedItem = (ComparableObjectItem) TestUtils.serialised(originalItem);

        // Assert
        assertNotSame(originalItem, deserializedItem, "Serialization must create a new object instance.");
        assertEquals(originalItem, deserializedItem, "The deserialized object should be equal to the original.");
    }
}