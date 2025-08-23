package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeTestTest9 {

    @Test
    public void hasValue() {
        Attribute a1 = new Attribute("one", "");
        Attribute a2 = new Attribute("two", null);
        Attribute a3 = new Attribute("thr", "thr");
        assertTrue(a1.hasDeclaredValue());
        assertFalse(a2.hasDeclaredValue());
        assertTrue(a3.hasDeclaredValue());
    }
}
