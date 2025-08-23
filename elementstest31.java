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

public class ElementsTestTest31 {

    @Test
    public void comments() {
        Document doc = Jsoup.parse("<!-- comment1 --><p><!-- comment2 --><p class=two><!-- comment3 -->");
        List<Comment> comments = doc.select("p").comments();
        assertEquals(2, comments.size());
        assertEquals(" comment2 ", comments.get(0).getData());
        assertEquals(" comment3 ", comments.get(1).getData());
        List<Comment> comments1 = doc.select("p.two").comments();
        assertEquals(1, comments1.size());
        assertEquals(" comment3 ", comments1.get(0).getData());
    }
}
