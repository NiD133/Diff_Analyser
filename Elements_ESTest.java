package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;

public class ElementsTest {

    @Test
    public void testConstructor() {
        // Test the no-arg constructor
        Elements elements = new Elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testEmptyElements() {
        Elements elements = new Elements();
        assertEquals(0, elements.size());
        elements = elements.eq(0);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAttr() {
        Document document = Jsoup.parse("<div id='test'>Test</div>");
        Elements elements = document.select("div");
        assertEquals("test", elements.attr("id"));
        elements.attr("class", "test-class");
        assertEquals("test-class", elements.attr("class"));
    }

    @Test
    public void testRemoveAttr() {
        Document document = Jsoup.parse("<div id='test' class='test-class'>Test</div>");
        Elements elements = document.select("div");
        elements.removeAttr("id");
        assertNull(elements.attr("id"));
        elements.removeAttr("class");
        assertNull(elements.attr("class"));
    }

    @Test
    public void testHasAttr() {
        Document document = Jsoup.parse("<div id='test'>Test</div>");
        Elements elements = document.select("div");
        assertTrue(elements.hasAttr("id"));
        assertFalse(elements.hasAttr("class"));
    }

    @Test
    public void testFilter() {
        Document document = Jsoup.parse("<div id='test'>Test</div><div id='test2'>Test 2</div>");
        Elements elements = document.select("div");
        Elements filtered = elements.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                return NodeFilter.FilterResult.CONTINUE;
            }
            @Override
            public FilterResult tail(Node node, int depth) {
                return NodeFilter.FilterResult.CONTINUE;
            }
        });
        assertEquals(2, filtered.size());
    }

    @Test
    public void testTraverse() {
        Document document = Jsoup.parse("<div id='test'>Test</div><div id='test2'>Test 2</div>");
        Elements elements = document.select("div");
        elements.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                // Perform some action on the node
            }
            @Override
            public void tail(Node node, int depth) {
                // Perform some action on the node
            }
        });
    }

    @Test
    public void testEachText() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        List<String> texts = elements.eachText();
        assertEquals(2, texts.size());
        assertEquals("Test", texts.get(0));
        assertEquals("Test 2", texts.get(1));
    }

    @Test
    public void testEachAttr() {
        Document document = Jsoup.parse("<div id='test' class='test-class'>Test</div>");
        Elements elements = document.select("div");
        List<String> ids = elements.eachAttr("id");
        assertEquals(1, ids.size());
        assertEquals("test", ids.get(0));
    }

    @Test
    public void testHtml() {
        Document document = Jsoup.parse("<div>Test</div>");
        Elements elements = document.select("div");
        assertEquals("<div>Test</div>", elements.html());
        elements.html("<span>New HTML</span>");
        assertEquals("<div><span>New HTML</span></div>", elements.html());
    }

    // Test sibling methods
    @Test
    public void testNext() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div:first-child");
        Elements next = elements.next();
        assertEquals(1, next.size());
        assertEquals("Test 2", next.text());
    }

    @Test
    public void testPrev() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div:last-child");
        Elements prev = elements.prev();
        assertEquals(1, prev.size());
        assertEquals("Test", prev.text());
    }

    @Test
    public void testNextAll() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div><div>Test 3</div>");
        Elements elements = document.select("div:first-child");
        Elements nextAll = elements.nextAll();
        assertEquals(2, nextAll.size());
        assertEquals("Test 2", nextAll.get(0).text());
        assertEquals("Test 3", nextAll.get(1).text());
    }

    @Test
    public void testPrevAll() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div><div>Test 3</div>");
        Elements elements = document.select("div:last-child");
        Elements prevAll = elements.prevAll();
        assertEquals(2, prevAll.size());
        assertEquals("Test", prevAll.get(0).text());
        assertEquals("Test 2", prevAll.get(1).text());
    }

    // Test list methods
    @Test
    public void testGet() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        Element element = elements.get(0);
        assertEquals("Test", element.text());
    }

    @Test
    public void testSet() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        Element newElement = Jsoup.parse("<span>New Element</span>").selectFirst("span");
        elements.set(0, newElement);
        assertEquals("New Element", elements.get(0).text());
    }

    @Test
    public void testRemove() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        Element removed = elements.remove(0);
        assertEquals(1, elements.size());
        assertEquals("Test 2", elements.get(0).text());
    }

    @Test
    public void testDeselect() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        Element deselected = elements.deselect(0);
        assertEquals(1, elements.size());
        assertEquals("Test 2", elements.get(0).text());
        // Verify that the deselected element is still present in the DOM
        assertEquals(2, document.select("div").size());
    }

    // Test miscellaneous methods
    @Test
    public void testEmpty() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        elements.empty();
        assertEquals(2, elements.size());
        assertEquals("", elements.get(0).text());
        assertEquals("", elements.get(1).text());
    }

    @Test
    public void testRemoveAll() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div>");
        Elements elements = document.select("div");
        elements.removeAll(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testRetainAll() {
        Document document = Jsoup.parse("<div>Test</div><div>Test 2</div><div>Test 3</div>");
        Elements elements = document.select("div");
        Elements toRetain = document.select("div:lt(2)");
        elements.retainAll(toRetain);
        assertEquals(2, elements.size());
        assertEquals("Test", elements.get(0).text());
        assertEquals("Test 2", elements.get(1).text());
    }
}