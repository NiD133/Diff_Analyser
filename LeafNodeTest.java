package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeafNodeTest {

    // The tests below verify that LeafNode implementations (e.g., TextNode, Comment) and Elements
    // do not eagerly allocate attribute maps. Attributes should appear only when required (e.g., when
    // a mutating call like addClass is made). We use subtreeHasAnyAttributes to check if any node in
    // a given subtree currently has attributes.

    @Test
    void documentWithBaseUri_hasOnlyDocumentAttributes() {
        String html = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document doc = Jsoup.parse(html, "https://example.com/");

        // Base URI is stored as an attribute on the Document
        assertSubtreeHasAttributes(doc);

        // The HTML subtree should still be attribute-free
        Element htmlEl = doc.child(0); // <html>
        assertSubtreeHasNoAttributes(htmlEl);
    }

    @Test
    void documentWithoutBaseUri_hasNoAttributesAnywhere() {
        Document doc = Jsoup.parse("<div>None</div>");
        assertSubtreeHasNoAttributes(doc);
    }

    @Test
    void readOnlyOperations_doNotMaterializeAttributes() {
        String html = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document doc = Jsoup.parse(html, "https://example.com/");
        Element htmlEl = doc.child(0); // <html>

        // Start clean: no attributes in the <html> subtree
        assertSubtreeHasNoAttributes(htmlEl);

        // Rendering should not force attribute allocation
        String ignored = doc.outerHtml();
        assertSubtreeHasNoAttributes(htmlEl);

        // Selecting existing elements should not force attribute allocation
        Element p = doc.selectFirst("p");
        assertNotNull(p);
        assertEquals(1, doc.select("p").size());
        assertSubtreeHasNoAttributes(htmlEl);

        // Non-matching selection should also not force allocation
        doc.select("p.none");
        assertSubtreeHasNoAttributes(htmlEl);

        // Reading attributes/classes should not force allocation
        assertEquals("", p.id());
        assertFalse(p.hasClass("Foobs"));
        assertSubtreeHasNoAttributes(htmlEl);
    }

    @Test
    void mutatingAttributes_createsAndThen_clearingRemoves() {
        String html = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        Document doc = Jsoup.parse(html, "https://example.com/");
        Element htmlEl = doc.child(0); // <html>
        Element p = doc.selectFirst("p");
        assertNotNull(p);

        // Initially, no attributes in the <html> subtree or on <p>
        assertSubtreeHasNoAttributes(htmlEl);
        assertSubtreeHasNoAttributes(p);

        // Mutation should cause attributes to be allocated
        p.addClass("Foobs");
        assertTrue(p.hasClass("Foobs"));
        assertSubtreeHasAttributes(p);
        assertSubtreeHasAttributes(htmlEl); // because <p> now has an attribute

        // Verify live Attributes view reflects state and clearing removes attributes
        Attributes attrs = p.attributes();
        assertTrue(attrs.hasKey("class"));

        p.clearAttributes();
        assertSubtreeHasNoAttributes(p);
        assertSubtreeHasNoAttributes(htmlEl);
        assertFalse(attrs.hasKey("class"));
    }

    // Helper: does any node in this subtree currently have attributes?
    private static boolean subtreeHasAnyAttributes(Node root) {
        final boolean[] found = {false};
        root.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    found[0] = true;
                    return FilterResult.STOP;
                }
                return FilterResult.CONTINUE;
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
        });
        return found[0];
    }

    private static void assertSubtreeHasAttributes(Node root) {
        assertTrue(subtreeHasAnyAttributes(root), "Expected attributes somewhere under <" + root.nodeName() + "> subtree");
    }

    private static void assertSubtreeHasNoAttributes(Node root) {
        assertFalse(subtreeHasAnyAttributes(root), "Expected no attributes under <" + root.nodeName() + "> subtree");
    }
}