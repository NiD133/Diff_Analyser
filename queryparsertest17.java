package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest17 {

    @Test
    void hasNodeSelector() {
        String q = "p:has(::comment:contains(some text))";
        Evaluator e = QueryParser.parse(q);
        assertEquals("(And (Tag 'p')(Has (And (InstanceType '::comment')(ContainsValue ':contains(some text)'))))", sexpr(e));
        assertEquals(q, e.toString());
    }
}
