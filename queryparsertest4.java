package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTestTest4 {

    @Test
    public void testParsesMultiCorrectly() {
        String query = ".foo.qux[attr=bar] > ol.bar, ol > li + li";
        String parsed = sexpr(query);
        assertEquals("(Or (And (Tag 'li')(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li'))))(ImmediateParentRun (And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))(And (Tag 'ol')(Class '.bar'))))", parsed);
        /*
        <Or css=".foo.qux[attr=bar] > ol.bar, ol > li + li" cost="31">
          <And css="ol > li + li" cost="7">
            <Tag css="li" cost="1"></Tag>
            <ImmediatePreviousSibling css="ol > li + " cost="6">
              <ImmediateParentRun css="ol > li" cost="4">
                <Tag css="ol" cost="1"></Tag>
                <Tag css="li" cost="1"></Tag>
              </ImmediateParentRun>
            </ImmediatePreviousSibling>
          </And>
          <ImmediateParentRun css=".foo.qux[attr=bar] > ol.bar" cost="24">
            <And css=".foo.qux[attr=bar]" cost="15">
              <AttributeWithValue css="[attr=bar]" cost="3"></AttributeWithValue>
              <Class css=".foo" cost="6"></Class>
              <Class css=".qux" cost="6"></Class>
            </And>
            <And css="ol.bar" cost="7">
              <Tag css="ol" cost="1"></Tag>
              <Class css=".bar" cost="6"></Class>
            </And>
          </ImmediateParentRun>
        </Or>
         */
    }
}
