package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling the deprecated method {@code addRdfDescription} with content
     * that is not valid XML throws an {@code IOException}.
     */
    @Test
    public void addRdfDescription_withInvalidXmlContent_shouldThrowIOException() throws IOException {
        // Arrange: Set up the test objects and inputs.
        // Use a ByteArrayOutputStream as a valid, non-null stream. While the method under test
        // doesn't use the stream, this is safer than passing null and prevents potential NPEs.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmpWriter xmpWriter = new XmpWriter(outputStream, Collections.emptyMap());

        String namespace = "UTF-16BE"; // Value from the original generated test.
        String invalidXmlContent = "com.itextpdf.text.io.FileChannelRandomAccessSource";

        // Act & Assert: Execute the method and verify the outcome.
        try {
            xmpWriter.addRdfDescription(namespace, invalidXmlContent);
            fail("Expected an IOException to be thrown for invalid XML content.");
        } catch (IOException e) {
            // The method is expected to fail while attempting to parse the invalid XML string.
            // The original test indicated an "XML parsing failure" message.
            assertEquals("XML parsing failure", e.getMessage());
        }
    }
}