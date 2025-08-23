package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on edge cases
 * like handling of malformed Unicode sequences.
 */
public class UTF8WriterTest {

    /**
     * Tests that writing a character that is not a low surrogate immediately after
     * writing a high surrogate results in an IOException. This verifies the correct
     * handling of broken or illegal surrogate pairs.
     */
    @Test(timeout = 4000)
    public void shouldThrowIOExceptionWhenHighSurrogateIsFollowedByNonLowSurrogate() {
        // Arrange
        // A high surrogate is the first character in a two-character sequence
        // used to represent Unicode code points beyond the Basic Multilingual Plane.
        // 0xDBFF is the last valid high surrogate.
        final int highSurrogate = 0xDBFF;

        // This character is not a valid low surrogate, which should cause an error.
        final String subsequentInvalidChar = "c";

        // Set up the necessary context and a writer that writes to an in-memory buffer.
        IOContext ioContext = createIOContext();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        try {
            // Act
            // 1. Write the high surrogate. It gets buffered, awaiting the low surrogate.
            utf8Writer.write(highSurrogate);

            // 2. Attempt to write a character that cannot legally follow a high surrogate.
            utf8Writer.write(subsequentInvalidChar);

            // Assert: If we reach here, the test fails because no exception was thrown.
            fail("Expected an IOException for a broken surrogate pair, but none was thrown.");

        } catch (IOException e) {
            // Assert: Verify the exception is the one we expect.
            // The message should clearly state the illegal combination of characters.
            // 0x63 is the hexadecimal representation of the character 'c'.
            String expectedMessage = "Broken surrogate pair: first char 0xdbff, second 0x63; illegal combination";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    /**
     * Helper method to create a default IOContext for tests.
     */
    private IOContext createIOContext() {
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.rawReference(false, null),
                true
        );
    }
}