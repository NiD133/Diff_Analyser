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

public class ElementsTestTest46 {

    @Test
    public void iteratorRemovesFromDom() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());
        for (Iterator<Element> it = ps.iterator(); it.hasNext(); ) {
            Element el = it.next();
            if (el.text().contains("Two"))
                it.remove();
        }
        assertEquals(3, ps.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }
}
