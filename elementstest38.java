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

public class ElementsTestTest38 {

    @Test
    public void eachAttr() {
        Document doc = Jsoup.parse("<div><a href='/foo'>1</a><a href='http://example.com/bar'>2</a><a href=''>3</a><a>4</a>", "http://example.com");
        List<String> hrefAttrs = doc.select("a").eachAttr("href");
        assertEquals(3, hrefAttrs.size());
        assertEquals("/foo", hrefAttrs.get(0));
        assertEquals("http://example.com/bar", hrefAttrs.get(1));
        assertEquals("", hrefAttrs.get(2));
        assertEquals(4, doc.select("a").size());
        List<String> absAttrs = doc.select("a").eachAttr("abs:href");
        assertEquals(3, absAttrs.size());
        assertEquals(3, absAttrs.size());
        assertEquals("http://example.com/foo", absAttrs.get(0));
        assertEquals("http://example.com/bar", absAttrs.get(1));
        assertEquals("http://example.com", absAttrs.get(2));
    }
}
