package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on key validation.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() correctly sanitizes an attribute key for HTML syntax
     * by replacing invalid characters. According to the HTML specification, characters
     * like '=', '"', "'", '/', and spaces are not permitted in unquoted attribute names.
     */
    @Test
    public void getValidKeyForHtmlReplacesInvalidCharacter() {
        // Arrange: Define the input key with an invalid character ('=') for HTML syntax.
        String invalidKey = "invalid-key=";
        String expectedKey = "invalid-key_";
        Syntax syntax = Syntax.html;

        // Act: Sanitize the key using the method under test.
        String actualKey = Attribute.getValidKey(invalidKey, syntax);

        // Assert: Confirm that the invalid character was replaced with an underscore.
        assertEquals(expectedKey, actualKey);
    }
}