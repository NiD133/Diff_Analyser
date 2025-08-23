package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the serialization of {@link CharRange}.
 */
class CharRangeSerializationTest extends AbstractLangTest {

    @Test
    @DisplayName("A single-character CharRange should be correctly restored after serialization")
    void serializationOfSingleCharRangeShouldPreserveObject() {
        // Arrange
        final CharRange originalRange = CharRange.is('a');

        // Act
        final CharRange clonedRange = SerializationUtils.clone(originalRange);

        // Assert
        assertEquals(originalRange, clonedRange);
    }

    @Test
    @DisplayName("An inclusive CharRange should be correctly restored after serialization")
    void serializationOfInclusiveRangeShouldPreserveObject() {
        // Arrange
        final CharRange originalRange = CharRange.isIn('a', 'e');

        // Act
        final CharRange clonedRange = SerializationUtils.clone(originalRange);

        // Assert
        assertEquals(originalRange, clonedRange);
    }

    @Test
    @DisplayName("A negated CharRange should be correctly restored after serialization")
    void serializationOfNegatedRangeShouldPreserveObject() {
        // Arrange
        final CharRange originalRange = CharRange.isNotIn('a', 'e');

        // Act
        final CharRange clonedRange = SerializationUtils.clone(originalRange);

        // Assert
        assertEquals(originalRange, clonedRange);
    }
}