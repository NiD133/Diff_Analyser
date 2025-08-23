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

public class ElementsTestTest52 {

    @Test
    void expectFirst() {
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element span = doc.children().expectFirst("span");
        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }
}
