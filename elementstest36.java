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

public class ElementsTestTest36 {

    @Test
    public void siblings() {
        Document doc = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12</div>");
        // gets p4 and p10
        Elements els = doc.select("p:eq(3)");
        assertEquals(2, els.size());
        Elements next = els.next();
        assertEquals(2, next.size());
        assertEquals("5", next.first().text());
        assertEquals("11", next.last().text());
        assertEquals(0, els.next("p:contains(6)").size());
        final Elements nextF = els.next("p:contains(5)");
        assertEquals(1, nextF.size());
        assertEquals("5", nextF.first().text());
        Elements nextA = els.nextAll();
        assertEquals(4, nextA.size());
        assertEquals("5", nextA.first().text());
        assertEquals("12", nextA.last().text());
        Elements nextAF = els.nextAll("p:contains(6)");
        assertEquals(1, nextAF.size());
        assertEquals("6", nextAF.first().text());
        Elements prev = els.prev();
        assertEquals(2, prev.size());
        assertEquals("3", prev.first().text());
        assertEquals("9", prev.last().text());
        assertEquals(0, els.prev("p:contains(1)").size());
        final Elements prevF = els.prev("p:contains(3)");
        assertEquals(1, prevF.size());
        assertEquals("3", prevF.first().text());
        Elements prevA = els.prevAll();
        assertEquals(6, prevA.size());
        assertEquals("3", prevA.first().text());
        assertEquals("7", prevA.last().text());
        Elements prevAF = els.prevAll("p:contains(1)");
        assertEquals(1, prevAF.size());
        assertEquals("1", prevAF.first().text());
    }
}
