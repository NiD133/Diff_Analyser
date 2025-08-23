package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest1 {

    @Test
    public void testConsumeSubQuery() {
        Document doc = Jsoup.parse("<html><head>h</head><body>" + "<li><strong>l1</strong></li>" + "<a><li><strong>l2</strong></li></a>" + "<p><strong>yes</strong></p>" + "</body></html>");
        // selecting immediate from body
        assertEquals("l1 yes", doc.body().select(">p>strong,>li>strong").text());
        // space variants
        assertEquals("l1 yes", doc.body().select(" > p > strong , > li > strong").text());
        assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
        assertEquals("l2 yes", doc.select("body>*>li>strong,body>p>strong").text());
        assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
    }
}
