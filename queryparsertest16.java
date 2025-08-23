package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest16 {

    @Test
    void consecutiveCombinators() {
        Selector.SelectorParseException exception1 = assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse("div>>p"));
        assertEquals("Could not parse query 'div>>p': unexpected token at '>p'", exception1.getMessage());
        Selector.SelectorParseException exception2 = assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse("+ + div"));
        assertEquals("Could not parse query '+ + div': unexpected token at '+ div'", exception2.getMessage());
    }
}
