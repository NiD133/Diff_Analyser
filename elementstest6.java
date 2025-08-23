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

public class ElementsTestTest6 {

    @Test
    public void absAttr() {
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements one = doc.select("#1");
        Elements two = doc.select("#2");
        Elements both = doc.select("a");
        assertEquals("", one.attr("abs:href"));
        assertEquals("https://jsoup.org", two.attr("abs:href"));
        assertEquals("https://jsoup.org", both.attr("abs:href"));
    }
}
