package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeTestTest2 {

    @Test
    public void htmlWithLtAndGtInValue() {
        Attribute attr = new Attribute("key", "<value>");
        assertEquals("key=\"&lt;value&gt;\"", attr.html());
    }
}
