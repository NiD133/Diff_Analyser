package org.jsoup.nodes;

import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

// The test class name and inheritance are preserved from the original auto-generated test.
public class Attribute_ESTestTest81 extends Attribute_ESTest_scaffolding {

    /**
     * Tests that the html() method correctly sanitizes invalid characters in an attribute's key
     * and escapes special characters in its value.
     */
    @Test
    public void htmlOutputSanitizesInvalidKeyAndEscapesValue() throws IOException {
        // Arrange
        // An attribute key with a double quote, which is an invalid character for an HTML attribute name.
        String keyWithInvalidChar = "key\"";
        // An attribute value that also contains a double quote, which needs to be escaped in the output.
        String valueWithQuote = "value\"";

        Attribute attribute = new Attribute(keyWithInvalidChar, valueWithQuote);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringWriter writer = new StringWriter();

        // The expected key is sanitized by replacing the invalid '\"' with '_'.
        // The expected value has its internal '\"' escaped to '&quot;'.
        // The final output format is key="value".
        String expectedHtml = "key_=\"value&quot;\"";

        // Act
        attribute.html(writer, outputSettings);
        String actualHtml = writer.toString();

        // Assert
        assertEquals(expectedHtml, actualHtml);
    }
}