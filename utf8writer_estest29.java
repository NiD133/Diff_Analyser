package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the static utility methods in the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Verifies that illegalSurrogateDesc() produces the correct error message
     * when given a high surrogate code point (the first part of a surrogate pair).
     */
    @Test
    public void illegalSurrogateDesc_withHighSurrogate_returnsCorrectErrorMessage() {
        // Arrange
        // 0xD800 is the first code point in the high-surrogate range.
        int highSurrogateCodePoint = 0xD800;
        String expectedMessage = "Unmatched first part of surrogate pair (0xd800)";

        // Act
        String actualMessage = UTF8Writer.illegalSurrogateDesc(highSurrogateCodePoint);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}