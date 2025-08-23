package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class, focusing on its
 * writing capabilities.
 */
public class SerializedStringTest {

    /**
     * Tests that writeQuotedUTF8() correctly writes a simple, non-escaped string
     * to an OutputStream and returns the accurate number of bytes written.
     */
    @Test
    public void writeQuotedUTF8_withSimpleString_writesContentAndReturnsLength() throws IOException {
        // Arrange: Create a SerializedString and an output stream to capture the result.
        SerializedString serializedString = new SerializedString("0");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        String expectedContent = "0";
        int expectedBytesWritten = 1;

        // Act: Write the string's quoted UTF-8 representation to the stream.
        int actualBytesWritten = serializedString.writeQuotedUTF8(outputStream);

        // Assert: Verify both the return value and the stream's content.
        assertEquals("The method should return the number of bytes written.",
                expectedBytesWritten, actualBytesWritten);

        String actualContent = outputStream.toString(StandardCharsets.UTF_8.name());
        assertEquals("The stream should contain the correct string content.",
                expectedContent, actualContent);
    }
}