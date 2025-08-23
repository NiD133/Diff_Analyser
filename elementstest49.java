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

public class ElementsTestTest49 {

    @Test
    public void replaceAll() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());
        ps.replaceAll(el -> {
            Element div = doc.createElement("div");
            div.text(el.text());
            return div;
        });
        // Check Elements
        for (Element p : ps) {
            assertEquals("div", p.tagName());
        }
        // check dom
        assertEquals("<div>One</div><div>Two</div><div>Three</div><div>Four</div>", TextUtil.normalizeSpaces(doc.body().html()));
    }
}
