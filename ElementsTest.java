package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Elements} functionality including filtering, attribute manipulation,
 * DOM traversal, and collection operations.
 */
public class ElementsTest {

    @Test
    public void filter_returnsElementsMatchingChainedSelectors() {
        // Setup
        String html = "<p>Excl</p><div class=headline><p>Hello</p><p>There</p></div><div class=headline><h1>Headline</h1></div>";
        Document doc = Jsoup.parse(html);

        // Execute: First get elements with class 'headline', then filter for 'p' elements inside them
        Elements els = doc.select(".headline").select("p");

        // Verify
        assertEquals(2, els.size());
        assertEquals("Hello", els.get(0).text());
        assertEquals("There", els.get(1).text());
    }

    @Test
    public void attributeOperations_handlePresenceRemovalAndSetting() {
        // Setup
        String html = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document doc = Jsoup.parse(html);

        // Test attribute presence
        Elements withTitle = doc.select("p[title]");
        assertEquals(2, withTitle.size());
        assertTrue(withTitle.hasAttr("title"));
        assertFalse(withTitle.hasAttr("class"));
        assertEquals("foo", withTitle.attr("title"));

        // Test attribute removal
        withTitle.removeAttr("title");
        assertEquals(2, withTitle.size());  // Existing collection unchanged
        assertEquals(0, doc.select("p[title]").size());  // DOM updated

        // Test attribute setting
        Elements allPs = doc.select("p").attr("style", "classy");
        assertEquals(4, allPs.size());
        assertEquals("classy", allPs.last().attr("style"));
        assertEquals("bar", allPs.last().attr("class"));  // Existing attr preserved
    }

    @Test
    public void hasAttr_checksExistenceAcrossElements() {
        // Setup
        Document doc = Jsoup.parse("<p title=foo><p title=bar><p class=foo><p class=bar>");
        Elements ps = doc.select("p");

        // Verify
        assertTrue(ps.hasAttr("class"));   // At least one element has class
        assertFalse(ps.hasAttr("style"));  // No element has style
    }

    // Additional attribute tests...

    @Test
    public void classOperations_addRemoveToggleAndCheckClasses() {
        // Setup
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements els = doc.select("p");

        // Verify initial state
        assertTrue(els.hasClass("red"));
        assertFalse(els.hasClass("blue"));

        // Modify classes
        els.addClass("blue")
           .removeClass("yellow")
           .toggleClass("mellow");  // Toggles: remove from first, add to second

        // Verify modifications
        assertEquals("blue", els.get(0).className());
        assertEquals("red green blue mellow", els.get(1).className());
    }

    @Test
    public void textMethods_handleTextContent() {
        // Setup
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p></p></div>");
        Elements divs = doc.select("div");

        // Verify text presence
        assertTrue(divs.hasText());                  // First div has text
        assertFalse(doc.select("div + div").hasText());  // Second div empty
    }

    // DOM manipulation tests...

    @Test
    public void unwrap_removesParentButKeepsChildren() {
        // Setup
        String html = "<div><font>One</font> <font><a href=\"/\">Two</a></font></div>";
        Document doc = Jsoup.parse(html);

        // Execute
        doc.select("font").unwrap();

        // Verify: Font tags removed, children preserved
        assertEquals("<div>\n One <a href=\"/\">Two</a>\n</div>", doc.body().html());
    }

    @Test
    public void empty_removesAllChildNodes() {
        // Setup
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);

        // Execute
        doc.select("p").empty();

        // Verify: Paragraph tags become empty
        assertEquals("<div><p></p> <p></p></div>", doc.body().html());
    }

    // Collection operation tests...

    @Test
    public void iteratorRemove_removesElementsFromDOM() {
        // Setup
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());

        // Execute: Remove elements containing "Two"
        for (Iterator<Element> it = ps.iterator(); it.hasNext(); ) {
            if (it.next().text().contains("Two")) {
                it.remove();
            }
        }

        // Verify
        assertEquals(3, ps.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }

    // Special selector tests...

    @Test
    public void selectFirst_returnsFirstMatchingElementOrNull() {
        // Setup
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");

        // Verify found case
        Element span = doc.children().selectFirst("span");
        assertNotNull(span);
        assertEquals("Jsoup", span.text());

        // Verify not found case
        Document noSpanDoc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        assertNull(noSpanDoc.children().selectFirst("span"));
    }
}