package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest1 {

    @Test
    void canRetrieveNewTagsSensitive() {
        Document doc = Jsoup.parse("<div><p>One</p></div>", "", Parser.htmlParser().settings(ParseSettings.preserveCase));
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
        assertEquals("FOO", foo.name());
        assertEquals("foo", foo.normalName());
        assertEquals(NamespaceHtml, foo.namespace());
        assertFalse(foo.isKnownTag());
        assertSame(foo, tags.get("FOO", NamespaceHtml));
        assertSame(foo, tags.valueOf("FOO", NamespaceHtml));
        assertNull(tags.get("FOO", "SomeOtherNamespace"));
    }
}
