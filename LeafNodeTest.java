package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    @Test
    public void testAttributeHandlingInNodes() {
        // Test to ensure attributes are not prematurely set on nodes
        String htmlContent = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document document = Jsoup.parse(htmlContent);

        // Initially, only the document should have attributes (base URI)
        assertTrue(hasAnyAttributes(document));

        Element htmlElement = document.child(0);
        assertFalse(hasAnyAttributes(htmlElement));

        // Check that outerHtml does not affect attributes
        String outerHtml = document.outerHtml();
        assertFalse(hasAnyAttributes(htmlElement));

        // Select the <p> element and verify its attributes
        Elements paragraphElements = document.select("p");
        Element paragraph = paragraphElements.first();
        assertEquals(1, paragraphElements.size());
        assertFalse(hasAnyAttributes(htmlElement));

        // Ensure no attributes are added when selecting non-existent elements
        Elements nonExistentElements = document.select("p.none");
        assertFalse(hasAnyAttributes(htmlElement));

        // Verify initial state of the <p> element
        String paragraphId = paragraph.id();
        assertEquals("", paragraphId);
        assertFalse(paragraph.hasClass("Foobs"));
        assertFalse(hasAnyAttributes(htmlElement));

        // Add a class to the <p> element and verify attributes
        paragraph.addClass("Foobs");
        assertTrue(paragraph.hasClass("Foobs"));
        assertTrue(hasAnyAttributes(htmlElement));
        assertTrue(hasAnyAttributes(paragraph));

        // Check attributes directly and clear them
        Attributes paragraphAttributes = paragraph.attributes();
        assertTrue(paragraphAttributes.hasKey("class"));
        paragraph.clearAttributes();
        assertFalse(hasAnyAttributes(paragraph));
        assertFalse(hasAnyAttributes(htmlElement));
        assertFalse(paragraphAttributes.hasKey("class"));
    }

    /**
     * Helper method to determine if a node or any of its children have attributes.
     *
     * @param node the node to check
     * @return true if any attributes are found, false otherwise
     */
    private boolean hasAnyAttributes(Node node) {
        final boolean[] foundAttributes = new boolean[1];
        node.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    foundAttributes[0] = true;
                    return FilterResult.STOP;
                }
                return FilterResult.CONTINUE;
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
        });
        return foundAttributes[0];
    }
}