package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Tests that getValidKey() correctly sanitizes an attribute name for HTML syntax.
     * According to the Jsoup implementation, invalid characters for an HTML attribute name
     * (like spaces and single quotes) are replaced with an underscore (_).
     */
    @Test
    public void getValidKeySanitizesInvalidHtmlCharacters() {
        // Arrange
        String invalidKey = " w'8l4N";
        String expectedSanitizedKey = "_w_8l4N";
        Syntax htmlSyntax = Syntax.html;

        // Act
        String actualSanitizedKey = Attribute.getValidKey(invalidKey, htmlSyntax);

        // Assert
        assertEquals(expectedSanitizedKey, actualSanitizedKey);
    }
}