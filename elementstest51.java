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

public class ElementsTestTest51 {

    @Test
    void selectFirstNullOnNoMatch() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Element span = doc.children().selectFirst("span");
        assertNull(span);
    }
}
