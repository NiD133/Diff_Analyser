package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on its handling of invalid surrogate pairs.
 */
public class UTF8WriterTest {

    /**
     * Tests that writing a lone low surrogate character (the second part of a surrogate pair)
     * without a preceding high surrogate correctly throws an IOException.
     * In UTF-8 encoding, surrogate pairs must be complete to be valid.
     */
    @Test(timeout = 4000)
    public void write_whenGivenUnmatchedLowSurrogate_shouldThrowIOException() {
        // Arrange: Set up the necessary IOContext and a UTF8Writer instance.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.redacted(),
                false); // 'false' indicates this context is not for recycling.
        OutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // An invalid character: a low surrogate (0xDC00-0xDFFF) without a preceding high surrogate.
        // We use the highest possible value for a low surrogate for this test.
        int unmatchedLowSurrogate = UTF8Writer.SURR2_LAST; // This constant equals 57343 (0xDFFF).

        try {
            // Act: Attempt to write the lone low surrogate character.
            utf8Writer.write(unmatchedLowSurrogate);
            // Assert: If no exception is thrown, the test should fail.
            fail("Expected an IOException for writing an unmatched low surrogate, but none was thrown.");
        } catch (IOException e) {
            // Assert: Verify that the exception message is correct and informative.
            String expectedMessage = "Unmatched second part of surrogate pair (0xdfff)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}