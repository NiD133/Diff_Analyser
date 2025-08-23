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
 * Unit tests for the {@link UTF8Writer} class, focusing on surrogate pair handling.
 */
public class UTF8WriterTest {

    /**
     * Creates a default IOContext for testing purposes.
     * This helper method encapsulates boilerplate setup.
     */
    private IOContext createTestIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        // The ContentReference is not critical for this test, so a simple raw reference is sufficient.
        ContentReference contentReference = ContentReference.rawReference(false, null);
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                contentReference,
                true // isResourceManaged
        );
    }

    /**
     * Verifies that writing an invalid character (one that is not a low surrogate)
     * immediately after a high surrogate character throws an IOException. This
     * ensures correct validation of broken surrogate pairs.
     */
    @Test
    public void write_whenAppendingInvalidCharacterAfterHighSurrogate_shouldThrowIOException() {
        // Arrange
        IOContext context = createTestIOContext();
        // Using ByteArrayOutputStream as a simple sink for the writer's output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(context, outputStream);

        // A high surrogate is the first character of a surrogate pair (range U+D800 to U+DBFF).
        int highSurrogate = UTF8Writer.SURR1_FIRST; // This is 0xD800, which is 55296 in decimal.
        char invalidFollowingChar = 'M';

        // Act & Assert
        try {
            writer.write(highSurrogate);
            // Attempt to append a character that is not a valid low surrogate.
            writer.append(invalidFollowingChar);
            fail("Expected an IOException for a broken surrogate pair, but none was thrown.");
        } catch (IOException e) {
            // Verify that the exception message clearly identifies the invalid pair.
            String expectedMessage = String.format(
                "Broken surrogate pair: first char 0x%x, second 0x%x; illegal combination",
                highSurrogate, (int) invalidFollowingChar
            );
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}