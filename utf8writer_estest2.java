package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link UTF8Writer} class, focusing on its exception-handling utilities.
 *
 * Note: The original test class name 'UTF8Writer_ESTestTest2' is unconventional,
 * likely auto-generated. A more standard name would be 'UTF8WriterTest'.
 */
public class UTF8Writer_ESTestTest2 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that the static helper method {@link UTF8Writer#illegalSurrogate(int)}
     * correctly throws an {@link IOException} with a specific, formatted message
     * when reporting an invalid surrogate code point.
     */
    @Test
    public void illegalSurrogateShouldThrowIOExceptionWithFormattedMessage() {
        // GIVEN an invalid surrogate code point (using max Unicode value for the test)
        int invalidCodePoint = 0x10FFFF; // 1114111 in decimal

        try {
            // WHEN the utility method is called to report the invalid code point
            UTF8Writer.illegalSurrogate(invalidCodePoint);

            // THEN an exception should have been thrown, so this line should not be reached
            fail("Expected an IOException to be thrown for an illegal surrogate code point.");

        } catch (IOException e) {
            // THEN verify the exception message is correctly formatted
            String expectedMessage = "Unmatched second part of surrogate pair (0x10ffff)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}