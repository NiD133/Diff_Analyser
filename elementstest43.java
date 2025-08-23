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

public class ElementsTestTest43 {

    @Test
    public void clear() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><div>Three</div>");
        Elements ps = doc.select("p");
        assertEquals(2, ps.size());
        ps.clear();
        assertEquals(0, ps.size());
        assertEquals(0, doc.select("p").size());
    }
}
