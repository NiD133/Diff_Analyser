package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter} focusing on encoding detection from the XML declaration.
 */
public class XmlStreamWriterTest {

    /**
     * Text containing the Euro symbol, which is representable in ISO-8859-15.
     */
    private static final String TEXT_WITH_EURO_SYMBOL = "euro: \u20AC";

    /**
     * Tests that the writer correctly detects the encoding specified in the XML
     * declaration and uses it for the output, overriding any configured default encoding.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void writerShouldDetectAndUseEncodingFromXmlDeclaration() throws IOException {
        // Arrange
        final String expectedEncoding = "ISO-8859-15";
        final String xmlContent = createXmlWithEncodingHeader(TEXT_WITH_EURO_SYMBOL, expectedEncoding);
        final byte[] expectedBytes = xmlContent.getBytes(expectedEncoding);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act: Write the XML using XmlStreamWriter, configured with a different default
        // encoding to ensure the detection mechanism works as expected.
        final String actualEncoding;
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .setCharset(StandardCharsets.UTF_8) // Set a default that should be overridden.
                .get()) {
            writer.write(xmlContent);
            actualEncoding = writer.getEncoding(); // Get the encoding detected by the writer.
        } // The try-with-resources block closes and flushes the writer.

        // Assert
        assertEquals(expectedEncoding, actualEncoding,
                "The writer should report the encoding detected from the XML declaration.");

        assertArrayEquals(expectedBytes, outputStream.toByteArray(),
                "The written bytes should be encoded using the detected encoding.");
    }

    /**
     * Helper method to create an XML string with a specified encoding in the declaration.
     *
     * @param text     The text content for the XML.
     * @param encoding The encoding to specify in the XML declaration.
     * @return A complete XML string.
     */
    private static String createXmlWithEncodingHeader(final String text, final String encoding) {
        final String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDeclaration + "\n<text>" + text + "</text>";
    }
}