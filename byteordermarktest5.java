package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteOrderMark}.
 */
class ByteOrderMarkTest {

    @Test
    @DisplayName("getCharsetName() should return the name provided in the constructor")
    void getCharsetNameShouldReturnNameProvidedInConstructor() {
        // Arrange
        final String expectedName1 = "test-charset-1";
        final ByteOrderMark bomWithOneByte = new ByteOrderMark(expectedName1, 1);

        final String expectedName2 = "test-charset-2";
        final ByteOrderMark bomWithTwoBytes = new ByteOrderMark(expectedName2, 1, 2);

        final String expectedName3 = "test-charset-3";
        final ByteOrderMark bomWithThreeBytes = new ByteOrderMark(expectedName3, 1, 2, 3);

        // Act & Assert
        assertEquals(expectedName1, bomWithOneByte.getCharsetName());
        assertEquals(expectedName2, bomWithTwoBytes.getCharsetName());
        assertEquals(expectedName3, bomWithThreeBytes.getCharsetName());
    }
}