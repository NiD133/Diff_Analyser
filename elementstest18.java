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

public class ElementsTestTest18 {

    @Test
    public void wrapDiv() {
        String h = "<p><b>This</b> is <b>jsoup</b>.</p> <p>How do you like it?</p>";
        Document doc = Jsoup.parse(h);
        doc.select("p").wrap("<div></div>");
        assertEquals("<div>\n <p><b>This</b> is <b>jsoup</b>.</p>\n</div>\n<div>\n <p>How do you like it?</p>\n</div>", doc.body().html());
    }
}
