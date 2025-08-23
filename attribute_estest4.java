package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the static helper methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() correctly sanitizes an attribute key for XML syntax
     * by replacing invalid characters (like '>') and trimming whitespace.
     */
    @Test
    public void getValidKeyReplacesInvalidCharactersForXmlSyntax() {
        // Arrange
        String invalidKey = "z>Su ";
        Syntax syntax = Syntax.xml;
        String expectedSanitizedKey = "z_Su_";

        // Act
        String actualSanitizedKey = Attribute.getValidKey(invalidKey, syntax);

        // Assert
        assertEquals(expectedSanitizedKey, actualSanitizedKey);
    }
}