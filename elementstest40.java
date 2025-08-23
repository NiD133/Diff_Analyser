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

public class ElementsTestTest40 {

    @Test
    public void removeElementByIndex() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        Elements ps = doc.select("p");
        Element two = ps.get(1);
        assertTrue(ps.contains(two));
        Element old = ps.remove(1);
        assertSame(old, two);
        // removed from list
        assertEquals(2, ps.size());
        assertFalse(ps.contains(old));
        // removed from dom
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html());
    }
}
