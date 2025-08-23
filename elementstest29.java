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

public class ElementsTestTest29 {

    @Test
    public void traverse() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div>There</div>");
        final StringBuilder accum = new StringBuilder();
        doc.select("div").traverse(new NodeVisitor() {

            @Override
            public void head(Node node, int depth) {
                accum.append("<").append(node.nodeName()).append(">");
            }

            @Override
            public void tail(Node node, int depth) {
                accum.append("</").append(node.nodeName()).append(">");
            }
        });
        assertEquals("<div><p><#text></#text></p></div><div><#text></#text></div>", accum.toString());
    }
}
