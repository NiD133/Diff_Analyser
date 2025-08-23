package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    public void getValidKeyWithValidXmlKeyReturnsKeyUnchanged() {
        // Arrange
        String validKey = "AfterFrameset";
        Syntax syntax = Syntax.xml;

        // Act
        String result = Attribute.getValidKey(validKey, syntax);

        // Assert
        assertEquals(validKey, result);
    }
}