package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest8 {

    @Test
    public void exceptOnEmptySelector() {
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse(""));
        assertEquals("String must not be empty", exception.getMessage());
    }
}
