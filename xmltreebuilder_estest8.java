package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains the improved version of a generated test case for XmlTreeBuilder.
 * The original test class name is preserved for context.
 */
public class XmlTreeBuilder_ESTestTest8 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that processing a character token correctly adds a new TextNode
     * as a child of the current element on the builder's stack.
     */
    @Test
    public void insertCharacterTokenAddsTextNodeToCurrentElement() {
        // Arrange: Set up the test objects and state.
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        
        // Parsing an empty string initializes the builder and places the root Document
        // node on the stack, making it the current element.
        Document document = xmlBuilder.parse("", "");
        
        // Create a character token with specific data to verify its insertion.
        Token.Character characterToken = new Token.Character().data("Hello, XML!");

        // Act: Execute the method under test.
        xmlBuilder.insertCharacterFor(characterToken);

        // Assert: Verify the expected outcome.
        // 1. The document (the current element) should now have exactly one child node.
        assertEquals("The document should have one child node after insertion.", 1, document.childNodeSize());
        
        // 2. The child node should be an instance of TextNode.
        assertTrue("The child node should be a TextNode.", document.childNode(0) instanceof TextNode);
        
        // 3. The content of the new TextNode should match the token's data.
        TextNode textNode = (TextNode) document.childNode(0);
        assertEquals("The TextNode content is incorrect.", "Hello, XML!", textNode.getWholeText());
    }
}