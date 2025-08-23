package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest13 {

    @Test
    public void parsesOrAfterAttribute() {
        // https://github.com/jhy/jsoup/issues/2073
        String q = "#parent [class*=child], .some-other-selector .nested";
        String parsed = sexpr(q);
        assertEquals("(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))", parsed);
        assertEquals("(Or (Class '.some-other-selector')(And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent'))))", sexpr("#parent [class*=child], .some-other-selector"));
        assertEquals("(Or (And (Id '#el')(AttributeWithValueContaining '[class*=child]'))(Class '.some-other-selector'))", sexpr("#el[class*=child], .some-other-selector"));
        assertEquals("(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))", sexpr("#parent [class*=child], .some-other-selector .nested"));
    }
}
