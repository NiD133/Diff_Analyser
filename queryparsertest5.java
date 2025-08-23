package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest5 {

    @Test
    void idDescenderClassOrder() {
        // https://github.com/jhy/jsoup/issues/2254
        // '#id .class' cost
        String query = "#id .class";
        String parsed = sexpr(query);
        assertEquals("(And (Class '.class')(Ancestor (Id '#id')))", parsed);
        /*
        <And css="#id .class" cost="22">
         <Class css=".class" cost="6"></Class>
         <Ancestor css="#id " cost="16">
          <Id css="#id" cost="2"></Id>
         </Ancestor>
        </And>
         */
    }
}
