package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeTestTest8 {

    @Test
    void settersAfterParentRemoval() {
        // tests key and value set on a retained attribute after disconnected from parent
        Attributes attrs = new Attributes();
        attrs.put("foo", "bar");
        Attribute attr = attrs.attribute("foo");
        assertNotNull(attr);
        attrs.remove("foo");
        assertEquals("foo", attr.getKey());
        assertEquals("bar", attr.getValue());
        attr.setKey("new");
        attr.setValue("newer");
        assertEquals("new", attr.getKey());
        assertEquals("newer", attr.getValue());
    }
}
