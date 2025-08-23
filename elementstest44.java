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

public class ElementsTestTest44 {

    @Test
    public void removeAll() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());
        //Two and Three
        Elements midPs = doc.select("p:gt(0):lt(3)");
        assertEquals(2, midPs.size());
        boolean removed = ps.removeAll(midPs);
        assertEquals(2, ps.size());
        assertTrue(removed);
        assertEquals(2, midPs.size());
        Elements divs = doc.select("div");
        assertEquals(1, divs.size());
        assertFalse(ps.removeAll(divs));
        assertEquals(2, ps.size());
        assertEquals("<p>One</p>\n<p>Four</p>\n<div>Div</div>", doc.body().html());
    }
}
