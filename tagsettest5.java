package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest5 {

    @Test
    void canCustomizeAll() {
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> tag.set(Tag.SelfClose));
        assertTrue(tags.get("script", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tags.valueOf("SCRIPT", NamespaceHtml).is(Tag.SelfClose));
        assertTrue(tags.valueOf("custom", NamespaceHtml).is(Tag.SelfClose));
        Tag foo = new Tag("foo", NamespaceHtml);
        assertFalse(foo.is(Tag.SelfClose));
        tags.add(foo);
        assertTrue(foo.is(Tag.SelfClose));
    }
}
