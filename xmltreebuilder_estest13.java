package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link XmlTreeBuilder} focusing on token processing.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that processing a Doctype token is successful and does not alter the
     * builder's default XML namespace, which is established during initialization.
     */
    @Test
    public void processDoctypeTokenReturnsTrueAndPreservesDefaultNamespace() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);

        // An initial parse is required to set the internal state of the builder,
        // including the default namespace. A simple parse of an empty string suffices.
        parser.parse("", "http://example.com");

        Token.Doctype doctypeToken = new Token.Doctype();

        // Act
        boolean result = xmlTreeBuilder.process(doctypeToken);

        // Assert
        assertTrue("Processing a doctype token should be a successful operation.", result);

        // The default namespace is set during the initial parse and should not be
        // affected by processing a subsequent doctype token.
        final String expectedNamespace = "http://www.w3.org/XML/1998/namespace";
        assertEquals("Default namespace should be preserved after processing a doctype.",
                expectedNamespace, xmlTreeBuilder.defaultNamespace());
    }
}