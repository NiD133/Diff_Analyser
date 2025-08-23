package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest3 {

    @Test
    public void testOrGetsCorrectPrecedence() {
        // tests that a selector "a b, c d, e f" evals to (a AND b) OR (c AND d) OR (e AND f)"
        // top level or, three child ands
        String query = "a b, c d, e f";
        String parsed = sexpr(query);
        assertEquals("(Or (And (Tag 'b')(Ancestor (Tag 'a')))(And (Tag 'd')(Ancestor (Tag 'c')))(And (Tag 'f')(Ancestor (Tag 'e'))))", parsed);
        /*
        <Or css="a b, c d, e f" cost="9">
          <And css="a b" cost="3">
            <Tag css="b" cost="1"></Tag>
            <Parent css="a " cost="2">
              <Tag css="a" cost="1"></Tag>
            </Parent>
          </And>
          <And css="c d" cost="3">
            <Tag css="d" cost="1"></Tag>
            <Parent css="c " cost="2">
              <Tag css="c" cost="1"></Tag>
            </Parent>
          </And>
          <And css="e f" cost="3">
            <Tag css="f" cost="1"></Tag>
            <Parent css="e " cost="2">
              <Tag css="e" cost="1"></Tag>
            </Parent>
          </And>
        </Or>
         */
    }
}
