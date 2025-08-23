package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange}.
 */
class CharRangeTest extends AbstractLangTest {

    @Test
    @DisplayName("CharRange.isIn() should create a non-negated range with correct properties")
    void isIn_createsCorrectInclusiveRange() {
        // Arrange
        final char startChar = 'a';
        final char endChar = 'e';

        // Act
        final CharRange range = CharRange.isIn(startChar, endChar);

        // Assert
        assertAll("Verify properties of range 'a'-'e'",
            () -> assertEquals(startChar, range.getStart(), "Start character should be 'a'"),
            () -> assertEquals(endChar, range.getEnd(), "End character should be 'e'"),
            () -> assertFalse(range.isNegated(), "Range should not be negated"),
            () -> assertEquals("a-e", range.toString(), "String representation should be 'a-e'")
        );
    }
}