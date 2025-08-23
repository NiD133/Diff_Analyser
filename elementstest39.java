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

public class ElementsTestTest39 {

    @Test
    public void setElementByIndex() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        Element newP = doc.createElement("p").text("New").attr("id", "new");
        Elements ps = doc.select("p");
        Element two = ps.get(1);
        Element old = ps.set(1, newP);
        assertSame(old, two);
        // replaced in list
        assertSame(newP, ps.get(1));
        // replaced in dom
        assertEquals("<p>One</p>\n<p id=\"new\">New</p>\n<p>Three</p>", doc.body().html());
    }
}
