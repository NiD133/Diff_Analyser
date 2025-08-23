package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class NodeIteratorTestTest13 {

    String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    static <T extends Node> void assertIterates(Iterator<T> it, String expected) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();
        while (it.hasNext()) {
            Node node = it.next();
            assertNotNull(node);
            assertNotSame(previous, node);
            trackSeen(node, actual);
            previous = node;
        }
        assertEquals(expected, actual.toString());
    }

    static void assertContents(Element el, String expected) {
        NodeIterator<Node> it = NodeIterator.from(el);
        assertIterates(it, expected);
    }

    public static void trackSeen(Node node, StringBuilder actual) {
        if (node instanceof Element) {
            Element el = (Element) node;
            actual.append(el.tagName());
            if (el.hasAttr("id"))
                actual.append("#").append(el.id());
        } else if (node instanceof TextNode)
            actual.append(((TextNode) node).text());
        else
            actual.append(node.nodeName());
        actual.append(";");
    }

    @Test
    void canModifyFilteredElements() {
        Document doc = Jsoup.parse(html);
        NodeIterator<Element> it = new NodeIterator<>(doc, Element.class);
        StringBuilder seen = new StringBuilder();
        while (it.hasNext()) {
            Element el = it.next();
            if (!el.ownText().isEmpty())
                el.text(el.ownText() + "++");
            trackSeen(el, seen);
        }
        assertEquals("#root;html;head;body;div#1;p;p;div#2;p;p;", seen.toString());
        assertContents(doc, "#root;html;head;body;div#1;p;One++;p;Two++;div#2;p;Three++;p;Four++;");
    }
}
