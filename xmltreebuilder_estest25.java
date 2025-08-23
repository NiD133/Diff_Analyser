package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link XmlTreeBuilder} class, focusing on its behavior
 * when handling specific token types.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that calling insertXmlDeclarationFor with a null token
     * correctly throws a NullPointerException. This test ensures the method
     * is robust against invalid null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void insertXmlDeclarationForWithNullTokenThrowsNullPointerException() {
        // Arrange: Create an instance of the tree builder.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act & Assert: Calling the method with a null argument should throw.
        xmlTreeBuilder.insertXmlDeclarationFor(null);
    }
}