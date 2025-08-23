package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This class focuses on specific behaviors of the XML tree construction process.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that calling popStackToClose with a tag name that is not on the
     * element stack does not alter the builder's current default namespace.
     * This ensures that non-matching end tags are safely ignored without corrupting
     * the parser's state.
     */
    @Test
    public void popStackToCloseWithUnmatchedTagDoesNotAffectDefaultNamespace() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);
        Document context = Document.createShell(""); // Use a clean document as the parsing context.
        String expectedNamespace = "http://www.w3.org/2000/svg";

        // Parse a fragment to establish a default namespace. This puts the builder
        // into a state where a default namespace is active.
        String fragmentWithNamespace = "<svg xmlns='" + expectedNamespace + "'><path/></svg>";
        parser.parseFragmentInput(fragmentWithNamespace, context, "http://example.com");

        // Sanity check to ensure the namespace was set correctly during the setup.
        assertEquals("Pre-condition failed: Default namespace was not set correctly.",
                expectedNamespace, xmlTreeBuilder.defaultNamespace());

        // Create an end tag for an element that is not on the stack.
        Token.EndTag unmatchedEndTag = new Token.EndTag();
        unmatchedEndTag.name("div"); // The stack contains html, body, svg. 'div' is not present.

        // Act
        // Attempt to pop the stack to the non-existent 'div' tag. This should be a no-op.
        xmlTreeBuilder.popStackToClose(unmatchedEndTag);

        // Assert
        // The key assertion: the default namespace should remain unchanged.
        assertEquals("Default namespace should not be changed by an unmatched popStackToClose call.",
                expectedNamespace, xmlTreeBuilder.defaultNamespace());
    }
}