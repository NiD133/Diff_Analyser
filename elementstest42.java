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

public class ElementsTestTest42 {

    @Test
    public void removeElementObjectNoops() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        String origHtml = doc.html();
        Element newP = doc.createElement("p").text("New");
        Elements ps = doc.select("p");
        int size = ps.size();
        assertFalse(ps.remove(newP));
        assertFalse(ps.remove(newP.childNodes()));
        assertEquals(origHtml, doc.html());
        assertEquals(size, ps.size());
    }
}
