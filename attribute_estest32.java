package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on key validation.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() returns the original string when provided with a key
     * that is already valid for HTML syntax.
     */
    @Test
    public void getValidKeyShouldReturnUnalteredKeyForValidHtmlSyntax() {
        // Arrange
        // This key contains various characters that are valid in HTML but might be
        // considered special, such as '$', '~', and '<'.
        String validHtmlKey = "1$uMT~<$2";
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.html;

        // Act
        String validatedKey = Attribute.getValidKey(validHtmlKey, syntax);

        // Assert
        // The method should recognize the key as valid for HTML and return it unchanged.
        assertEquals("The valid key should not have been modified", validHtmlKey, validatedKey);
    }
}