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
 * Contains tests for the {@link UTF8Writer} class, focusing on its handling
 * of Unicode surrogate pairs.
 */
public class UTF8WriterTest {

    /**
     * Tests that the UTF8Writer correctly throws an exception when an invalid
     * surrogate pair sequence is written. Specifically, this test verifies that
     * writing a high surrogate followed by another high surrogate results in an
     * IOException, as this is an illegal combination.
     */
    @Test
    public void write_whenHighSurrogateIsFollowedByAnotherHighSurrogate_shouldThrowIOException() {
        // Arrange
        // A high surrogate is the first character of a surrogate pair used to represent
        // Unicode characters beyond the Basic Multilingual Plane (BMP).
        // High surrogates are in the range U+D800 to U+DBFF.
        final int highSurrogate = 0xD800; // 55296 in decimal

        // Set up the writer with the necessary (but boilerplate) IOContext.
        IOContext ioContext = createIOContext();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        try {
            // Act
            // 1. Write the first high surrogate. The writer buffers this, expecting a
            //    low surrogate to follow to complete the pair.
            utf8Writer.write(highSurrogate);

            // 2. Write another high surrogate. This is an invalid sequence, which should
            //    trigger an exception.
            utf8Writer.write(highSurrogate);

            // Assert: If we reach here, the test fails because no exception was thrown.
            fail("Expected an IOException for a broken surrogate pair, but none was thrown.");
        } catch (IOException e) {
            // Assert: Verify the exception is the one we expect.
            String expectedMessage = "Broken surrogate pair: first char 0xd800, second 0xd800; illegal combination";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    /**
     * Helper method to create a default IOContext instance for tests.
     * This encapsulates the boilerplate setup.
     */
    private IOContext createIOContext() {
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.unknown(),
                true);
    }
}