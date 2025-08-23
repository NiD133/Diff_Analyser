package org.apache.commons.io.output;

import org.junit.Test;
import org.evosuite.runtime.mock.java.io.MockFile;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link XmlStreamWriter}.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the writer defaults to UTF-8 encoding when the written content
     * does not contain an XML encoding declaration.
     *
     * The XmlStreamWriter is designed to detect the encoding from the XML prolog
     * (e.g., <?xml version="1.0" encoding="UTF-16"?>). If no such prolog is
     * written, it should fall back to the default encoding provided at construction,
     * which is UTF-8 for the File-based constructor.
     */
    @Test
    public void getEncodingShouldReturnDefaultUtf8WhenNoXmlDeclarationIsWritten() throws IOException {
        // Arrange: Create a mock file and an XmlStreamWriter.
        // The constructor used defaults to UTF-8 encoding.
        File outputFile = new MockFile("test.xml");
        String contentWithoutXmlDeclaration = "<data>some content</data>";

        // Act & Assert: Use try-with-resources to ensure the writer is closed.
        try (XmlStreamWriter writer = new XmlStreamWriter(outputFile)) {
            // Write content that does not specify an encoding.
            writer.write(contentWithoutXmlDeclaration);

            // The writer should fall back to its default encoding.
            final String expectedEncoding = "UTF-8";
            final String actualEncoding = writer.getEncoding();

            assertEquals("The encoding should default to UTF-8 when no XML declaration is present.",
                         expectedEncoding, actualEncoding);
        }
    }
}