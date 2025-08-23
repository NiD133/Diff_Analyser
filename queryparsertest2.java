package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest2 {

    @Test
    public void testImmediateParentRun() {
        String query = "div > p > bold.brass";
        assertEquals("(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))", sexpr(query));
        /*
        <ImmediateParentRun css="div > p > bold.brass" cost="11">
          <Tag css="div" cost="1"></Tag>
          <Tag css="p" cost="1"></Tag>
          <And css="bold.brass" cost="7">
            <Tag css="bold" cost="1"></Tag>
            <Class css=".brass" cost="6"></Class>
          </And>
        </ImmediateParentRun>
         */
    }
}
