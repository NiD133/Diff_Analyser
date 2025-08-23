package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link XmlStreamWriter}.
 * This class focuses on encoding detection behavior.
 */
// The original test class name and scaffolding are kept for context.
public class XmlStreamWriter_ESTestTest22 extends XmlStreamWriter_ESTest_scaffolding {

    /**
     * Tests that the writer falls back to the default encoding (UTF-8) when an
     * XML declaration is written without an explicit 'encoding' attribute.
     * The writer should detect the start of the XML but find no encoding,
     * thus applying the default.
     */
    @Test
    public void shouldUseDefaultEncodingWhenXmlDeclarationHasNoEncodingAttribute() throws IOException {
        // Arrange
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // This constructor uses UTF-8 as the default encoding.
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);

        // Act
        // Write the XML declaration in two parts to test the writer's internal buffering.
        writer.append("<?xml version=\"1.0\"");
        writer.write("?>");

        // Assert
        assertEquals("The encoding should default to UTF-8", "UTF-8", writer.getEncoding());
    }
}