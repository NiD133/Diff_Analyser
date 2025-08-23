package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter} focusing on encoding detection behavior.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the writer defaults to the builder's default encoding (UTF-8)
     * when writing an XML string that does not contain an XML encoding declaration.
     * The builder for {@link XmlStreamWriter} is documented to default to UTF-8.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void writerShouldUseBuilderDefaultEncodingWhenXmlLacksEncodingDeclaration() throws IOException {
        // Arrange
        final String xmlContentWithoutHeader = "<text>text with no XML header</text>";
        final Charset expectedEncoding = StandardCharsets.UTF_8;
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        final String detectedEncoding;
        // Use the builder's default by not calling setCharset().
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
            .setOutputStream(outputStream)
            .get()) {
            writer.write(xmlContentWithoutHeader);
            // The encoding is detected upon the first write.
            detectedEncoding = writer.getEncoding();
        }

        // Assert
        final byte[] expectedBytes = xmlContentWithoutHeader.getBytes(expectedEncoding);
        final byte[] actualBytes = outputStream.toByteArray();

        assertAll(
            () -> assertEquals(expectedEncoding.name(), detectedEncoding,
                "Detected encoding should be the builder's default (UTF-8)."),
            () -> assertArrayEquals(expectedBytes, actualBytes,
                "The written byte array should be correctly encoded in UTF-8.")
        );
    }
}