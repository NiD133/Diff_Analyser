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

public class ElementsTestTest13 {

    @Test
    public void setHtml() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements ps = doc.select("p");
        ps.prepend("<b>Bold</b>").append("<i>Ital</i>");
        assertEquals("<p><b>Bold</b>Two<i>Ital</i></p>", TextUtil.stripNewlines(ps.get(1).outerHtml()));
        ps.html("<span>Gone</span>");
        assertEquals("<p><span>Gone</span></p>", TextUtil.stripNewlines(ps.get(1).outerHtml()));
    }
}
