package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on its static helper methods.
 */
public class UTF8WriterTest {

    /**
     * Tests that illegalSurrogateDesc() generates the correct error message
     * for an unmatched high surrogate (the first part of a surrogate pair).
     */
    @Test
    public void illegalSurrogateDescShouldReturnMessageForUnmatchedHighSurrogate() {
        // Arrange
        // 0xDBFF is the upper boundary for a high surrogate (the first part of a pair).
        // We are testing the error message when this character is found alone.
        int unmatchedHighSurrogate = 0xDBFF;
        String expectedMessage = "Unmatched first part of surrogate pair (0xdbff)";

        // Act
        String actualMessage = UTF8Writer.illegalSurrogateDesc(unmatchedHighSurrogate);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}