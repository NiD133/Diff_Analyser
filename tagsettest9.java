package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.junit.jupiter.api.Assertions.*;

public class TagSetTestTest9 {

    @Test
    void supportsMultipleCustomizers() {
        TagSet tags = TagSet.Html();
        tags.onNewTag(tag -> {
            if (tag.normalName().equals("script"))
                tag.set(Tag.SelfClose);
        });
        tags.onNewTag(tag -> {
            if (!tag.isKnownTag())
                tag.set(Tag.RcData);
        });
        assertTrue(tags.valueOf("script", NamespaceHtml).is(Tag.SelfClose));
        assertFalse(tags.valueOf("script", NamespaceHtml).is(Tag.RcData));
        assertTrue(tags.valueOf("custom-tag", NamespaceHtml).is(Tag.RcData));
    }
}
