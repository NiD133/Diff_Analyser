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

public class ElementsTestTest54 {

    @Test
    void selectFirstFromPreviousSelect() {
        Document doc = Jsoup.parse("<div><p>One</p></div><div><p><span>Two</span></p></div><div><p><span>Three</span></p></div>");
        Elements divs = doc.select("div");
        assertEquals(3, divs.size());
        Element span = divs.selectFirst("p span");
        assertNotNull(span);
        assertEquals("Two", span.text());
        // test roots
        // reselect self
        assertNotNull(span.selectFirst("span"));
        // no span>span
        assertNull(span.selectFirst(">span"));
        // reselect self, similar to element.select
        assertNotNull(divs.selectFirst("div"));
        // no div>div
        assertNull(divs.selectFirst(">div"));
    }
}
