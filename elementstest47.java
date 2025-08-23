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

public class ElementsTestTest47 {

    @Test
    public void removeIf() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());
        boolean removed = ps.removeIf(el -> el.text().contains("Two"));
        assertTrue(removed);
        assertEquals(3, ps.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
        assertFalse(ps.removeIf(el -> el.text().contains("Five")));
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }
}
