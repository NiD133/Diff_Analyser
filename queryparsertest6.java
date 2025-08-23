package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest6 {

    @Test
    public void exceptionOnUncloseAttribute() {
        Selector.SelectorParseException exception = assertThrows(Selector.SelectorParseException.class, () -> QueryParser.parse("section > a[href=\"]"));
        assertEquals("Did not find balanced marker at 'href=\"]'", exception.getMessage());
    }
}
