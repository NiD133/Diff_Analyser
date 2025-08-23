package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link BigDecimalParser} class, focusing on edge cases and error handling.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that calling {@code parse} with a character array and providing an offset
     * and length that are out of bounds correctly throws a {@code StringIndexOutOfBoundsException}.
     * This is expected behavior, as the underlying implementation will fail to create a
     * string from the invalid range.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void parseWithCharArray_whenBoundsAreInvalid_shouldThrowException() {
        // Arrange: An empty character array and out-of-bounds indices.
        char[] emptyCharArray = new char[0];
        int offset = 1; // Any non-zero offset is out of bounds for an empty array
        int length = 1;

        // Act: Attempt to parse a segment of the array that does not exist.
        // Assert: The @Test(expected=...) annotation asserts that a StringIndexOutOfBoundsException is thrown.
        BigDecimalParser.parse(emptyCharArray, offset, length);
    }
}