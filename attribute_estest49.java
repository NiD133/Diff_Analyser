package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class, focusing on key validation.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() replaces an invalid character (a single quote)
     * with an underscore when using HTML syntax.
     */
    @Test
    public void getValidKeyShouldReplaceInvalidCharacterForHtmlSyntax() {
        // Arrange
        String invalidKey = "AkY'Y";
        String expectedKey = "AkY_Y";
        Syntax syntax = Syntax.html;

        // Act
        String actualKey = Attribute.getValidKey(invalidKey, syntax);

        // Assert
        assertEquals("The single quote should be replaced with an underscore.", expectedKey, actualKey);
    }
}