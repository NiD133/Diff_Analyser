package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

/**
 * Test suite for {@link W3CDom}.
 * This class focuses on verifying the conversion of Jsoup documents to W3C DOM documents.
 */
public class W3CDomTest {

    /**
     * Tests that a Jsoup document containing a malformed DOCTYPE, which Jsoup's
     * lenient parser interprets as a comment, is correctly converted to a W3C DOM
     * with a corresponding comment node.
     */
    @Test
    public void convertsMalformedDoctypeAsComment() {
        // Arrange: Create a Jsoup document from a malformed DOCTYPE string.
        // Jsoup's HTML parser will parse "<!DOCMTYPE" as a comment node "<!--DOCMTYPE-->".
        String malformedHtml = "<!DOCMTYPE";
        Document jsoupDoc = Parser.parseBodyFragment(malformedHtml, "");

        // Act: Convert the Jsoup document to a W3C DOM document.
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);

        // Assert: Verify the structure and content of the resulting W3C document.
        assertNotNull("The converted W3C document should not be null.", w3cDoc);

        // The body fragment is wrapped in <html><body>...</body></html>.
        // We expect the comment to be inside the body element.
        Node bodyElement = w3cDoc.getElementsByTagName("body").item(0);
        assertNotNull("The W3C document should contain a <body> element.", bodyElement);
        assertTrue("The <body> element should have child nodes.", bodyElement.hasChildNodes());
        assertEquals("The <body> element should have exactly one child node.", 1, bodyElement.getChildNodes().getLength());

        Node childNode = bodyElement.getFirstChild();
        assertEquals("The child node should be a comment.", Node.COMMENT_NODE, childNode.getNodeType());

        Comment commentNode = (Comment) childNode;
        assertEquals("The comment content should match the parsed malformed DOCTYPE.", "DOCMTYPE", commentNode.getData());
    }
}