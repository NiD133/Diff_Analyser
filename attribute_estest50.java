package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on HTML serialization.
 */
public class AttributeTest {

    /**
     * Verifies that the html() method correctly sanitizes an attribute key
     * containing invalid characters (like a space) by replacing them.
     * According to jsoup's implementation, a space in an attribute key
     * should be replaced with an underscore.
     */
    @Test
    public void htmlShouldSanitizeInvalidCharactersInAttributeKey() {
        // Arrange
        String keyWithInvalidSpace = "XII YZ}5!";
        String value = "async";
        Attribute attribute = Attribute.createFromEncoded(keyWithInvalidSpace, value);

        // The expected HTML, with the invalid space in the key replaced by an underscore.
        String expectedHtml = "XII_YZ}5!=\"async\"";

        // Act
        String actualHtml = attribute.html();

        // Assert
        assertEquals("The HTML output should have a sanitized key.", expectedHtml, actualHtml);
    }
}