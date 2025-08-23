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

public class ElementsTestTest53 {

    @Test
    void expectFirstThrowsOnNoMatch() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        boolean threw = false;
        try {
            Element span = doc.children().expectFirst("span");
        } catch (IllegalArgumentException e) {
            threw = true;
            assertEquals("No elements matched the query 'span' in the elements.", e.getMessage());
        }
        assertTrue(threw);
    }
}
