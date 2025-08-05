package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafNodeTest {

    private Document doc;
    private Element html;
    private Element p;

    @BeforeEach
    void setUp() {
        String body = "<p>One <!-- Two --> Three<![CDATA[Four]]></p>";
        doc = Jsoup.parse(body, "https://example.com/");
        html = doc.child(0);
        p = doc.select("p").first();
    }

    @Test
    void documentWithBaseUri_hasAttributesInSubtree() {
        assertTrue(anyNodeInSubtreeHasAttributes(doc));
    }

    @Test
    void documentWithoutBaseUri_hasNoAttributesInSubtree() {
        Document doc = Jsoup.parse("<div>None</div>");
        assertFalse(anyNodeInSubtreeHasAttributes(doc));
    }

    @Test
    void htmlElementInitially_hasNoAttributesInSubtree() {
        assertFalse(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void callingOuterHtml_doesNotAddAttributesToHtmlSubtree() {
        String s = doc.outerHtml();
        assertFalse(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void selectingParagraph_doesNotAddAttributesToHtmlSubtree() {
        Elements els = doc.select("p");
        assertEquals(1, els.size());
        assertFalse(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void selectingNonExistentClass_doesNotAddAttributes() {
        Elements els = doc.select("p.none");
        assertTrue(els.isEmpty());
        assertFalse(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void checkingIdAndClass_doesNotAddAttributes() {
        String id = p.id();
        assertEquals("", id);
        assertFalse(p.hasClass("Foobs"));
        assertFalse(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void addingClass_addsAttributesToParagraph() {
        p.addClass("Foobs");
        assertTrue(p.hasClass("Foobs"));
        assertTrue(anyNodeInSubtreeHasAttributes(p));
    }

    @Test
    void addingClass_addsAttributesToHtmlSubtree() {
        p.addClass("Foobs");
        assertTrue(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void attributesObject_reflectsClassAfterAddition() {
        p.addClass("Foobs");
        Attributes attributes = p.attributes();
        assertTrue(attributes.hasKey("class"));
    }

    @Test
    void clearingAttributes_removesThemFromParagraph() {
        p.addClass("Foobs");
        p.clearAttributes();
        assertFalse(anyNodeInSubtreeHasAttributes(p));
    }

    @Test
    void clearingAttributes_removesThemFromHtmlSubtree() {
        p.addClass("Foobs");
        p.clearAttributes();
        assertFalse(anyNodeInSubtreeHasAttributes(html));
    }

    @Test
    void attributesObject_reflectsRemovalAfterClear() {
        p.addClass("Foobs");
        Attributes attributes = p.attributes();
        p.clearAttributes();
        assertFalse(attributes.hasKey("class"));
    }

    private boolean anyNodeInSubtreeHasAttributes(Node node) {
        final boolean[] found = new boolean[1];
        node.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                if (node.hasAttributes()) {
                    found[0] = true;
                    return FilterResult.STOP;
                } else {
                    return FilterResult.CONTINUE;
                }
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
        });
        return found[0];
    }
}