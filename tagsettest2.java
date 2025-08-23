package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest2 {

    @Test
    void canRetrieveNewTagsInsensitive() {
        Document doc = Jsoup.parse("<div><p>One</p></div>");
        TagSet tags = doc.parser().tagSet();
        // should be the full html set
        Tag meta = tags.get("meta", NamespaceHtml);
        assertNotNull(meta);
        assertTrue(meta.isKnownTag());
        Element p = doc.expectFirst("p");
        assertTrue(p.tag().isKnownTag());
        assertNull(tags.get("FOO", NamespaceHtml));
        p.tagName("FOO");
        Tag foo = p.tag();
        assertEquals("foo", foo.name());
        assertEquals("foo", foo.normalName());
        assertEquals(NamespaceHtml, foo.namespace());
        assertFalse(foo.isKnownTag());
        assertSame(foo, tags.get("foo", NamespaceHtml));
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml, doc.parser().settings()));
        assertNull(tags.get("foo", "SomeOtherNamespace"));
    }
}
