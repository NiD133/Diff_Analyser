package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Tests that {@link Attribute#getValidKey(String, Document.OutputSettings.Syntax)} correctly sanitizes an
     * invalid key containing only control characters (carriage returns, newlines) when in HTML mode.
     * The expected behavior is to replace the entire invalid string with a single underscore.
     */
    @Test
    public void getValidKeyReplacesControlCharactersInHtmlMode() {
        // Arrange
        String invalidKey = "\r\n\r\n";
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;
        String expectedKey = "_";

        // Act
        String actualKey = Attribute.getValidKey(invalidKey, syntax);

        // Assert
        assertEquals(expectedKey, actualKey);
    }
}