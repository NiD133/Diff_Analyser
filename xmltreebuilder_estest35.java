package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the defaultNamespace() method returns the correct, standard XML namespace URI.
     */
    @Test
    public void defaultNamespaceReturnsCorrectXmlNamespaceUri() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String expectedNamespace = "http://www.w3.org/XML/1998/namespace";

        // Act
        String actualNamespace = xmlTreeBuilder.defaultNamespace();

        // Assert
        assertEquals(expectedNamespace, actualNamespace);
    }
}