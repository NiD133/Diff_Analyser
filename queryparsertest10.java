package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest10 {

    @Test
    public void exceptOnUnhandledEvaluator() {
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse("div / foo"));
        assertEquals("Could not parse query 'div / foo': unexpected token at '/ foo'", exception.getMessage());
    }
}
