package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest4 {

    @Test
    void knownTags() {
        // tests that tags explicitly inserted via .add are 'known'; those that come implicitly via valueOf are not
        TagSet tags = TagSet.Html();
        Tag custom = new Tag("custom");
        assertEquals("custom", custom.name());
        assertEquals(NamespaceHtml, custom.namespace());
        // not yet
        assertFalse(custom.isKnownTag());
        Tag br = tags.get("br", NamespaceHtml);
        assertNotNull(br);
        assertTrue(br.isKnownTag());
        assertSame(br, tags.valueOf("br", NamespaceHtml));
        Tag foo = tags.valueOf("foo", NamespaceHtml);
        assertFalse(foo.isKnownTag());
        tags.add(custom);
        assertTrue(custom.isKnownTag());
        assertSame(custom, tags.get("custom", NamespaceHtml));
        assertSame(custom, tags.valueOf("custom", NamespaceHtml));
        Tag capCustom = tags.valueOf("Custom", NamespaceHtml);
        // cloned from a known tag, so is still known
        assertTrue(capCustom.isKnownTag());
        // known if set or clear called
        Tag c1 = new Tag("bar");
        assertFalse(c1.isKnownTag());
        c1.set(Tag.Block);
        assertTrue(c1.isKnownTag());
        c1.clear(Tag.Block);
        assertTrue(c1.isKnownTag());
        c1.clear(Tag.Known);
        assertFalse(c1.isKnownTag());
    }
}
