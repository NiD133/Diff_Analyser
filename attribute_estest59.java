package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() returns null for an empty input key,
     * as an empty string is not a valid attribute key.
     */
    @Test
    public void getValidKeyShouldReturnNullForEmptyKey() {
        // Arrange
        String emptyKey = "";
        Syntax syntax = Syntax.html;

        // Act
        String result = Attribute.getValidKey(emptyKey, syntax);

        // Assert
        assertNull("getValidKey should return null for an empty key string.", result);
    }
}