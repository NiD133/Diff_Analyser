package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest14 {

    @Test
    void parsesEscapedSubqueries() {
        String html = "<div class='-4a'>One</div> <div id='-4a'>Two</div>";
        Document doc = Jsoup.parse(html);
        String classQ = "div.-\\34 a";
        Element div1 = doc.expectFirst(classQ);
        assertEquals("One", div1.wholeText());
        String idQ = "#-\\34 a";
        Element div2 = doc.expectFirst(idQ);
        assertEquals("Two", div2.wholeText());
        String genClassQ = "html > body > div.-\\34 a";
        assertEquals(genClassQ, div1.cssSelector());
        assertSame(div1, doc.expectFirst(genClassQ));
        String deepIdQ = "html > body > #-\\34 a";
        assertEquals(idQ, div2.cssSelector());
        assertSame(div2, doc.expectFirst(deepIdQ));
        assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))", sexpr(genClassQ));
        assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a'))", sexpr(deepIdQ));
    }
}
