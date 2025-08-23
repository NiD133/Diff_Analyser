package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest17 {

    @Test
    public void alwaysEscapeLtAndGtInAttributeValues() {
        // https://github.com/jhy/jsoup/issues/2337
        String docHtml = "<a title='<p>One</p>'>One</a>";
        Document doc = Jsoup.parse(docHtml);
        Element element = doc.select("a").first();
        doc.outputSettings().escapeMode(base);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", element.outerHtml());
        doc.outputSettings().escapeMode(xhtml);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", element.outerHtml());
    }
}
