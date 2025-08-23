package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that getValidKey() returns an already-valid key unchanged for XML syntax.
     * The key "autofocus" is a valid attribute name and should not be modified.
     */
    @Test
    public void getValidKeyForValidXmlAttributeReturnsUnchanged() {
        // Arrange
        String validAttributeKey = "autofocus";
        Syntax syntax = Syntax.xml;

        // Act
        String result = Attribute.getValidKey(validAttributeKey, syntax);

        // Assert
        assertEquals(validAttributeKey, result);
    }
}