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

public class ElementsTestTest2 {

    @Test
    public void attributes() {
        String h = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document doc = Jsoup.parse(h);
        Elements withTitle = doc.select("p[title]");
        assertEquals(2, withTitle.size());
        assertTrue(withTitle.hasAttr("title"));
        assertFalse(withTitle.hasAttr("class"));
        assertEquals("foo", withTitle.attr("title"));
        withTitle.removeAttr("title");
        // existing Elements are not reevaluated
        assertEquals(2, withTitle.size());
        assertEquals(0, doc.select("p[title]").size());
        Elements ps = doc.select("p").attr("style", "classy");
        assertEquals(4, ps.size());
        assertEquals("classy", ps.last().attr("style"));
        assertEquals("bar", ps.last().attr("class"));
    }
}
