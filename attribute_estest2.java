package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Tests that {@link Attribute#getValidKey(String, Syntax)} returns the original key
     * when it is already a valid key for the XML syntax.
     */
    @Test
    public void getValidKeyShouldReturnOriginalKeyWhenAlreadyValidForXml() {
        // Arrange
        String validXmlKey = "ZVY";
        Syntax syntax = Syntax.xml;

        // Act
        String resultKey = Attribute.getValidKey(validXmlKey, syntax);

        // Assert
        assertEquals("The original valid key should be returned unchanged.", validXmlKey, resultKey);
    }
}