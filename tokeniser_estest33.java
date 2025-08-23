package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the HTML Parser's ability to handle malformed or unusual inputs.
 */
public class ParserLeniencyTest {

    /**
     * Tests that the parser can handle a body fragment containing an unclosed tag.
     * Jsoup's parser is expected to be lenient and automatically close the tag at the end of the fragment.
     */
    @Test
    public void handlesUnclosedTagInBodyFragment() {
        // Arrange: An HTML fragment with plain text and an unclosed <i> tag.
        String htmlFragment = "C(p;<i>L_mFu^";
        String expectedBodyHtml = "C(p;<i>L_mFu^</i>"; // Jsoup should auto-close the <i> tag.

        // Act: Parse the string as a body fragment.
        // The base URI is not relevant for this test, so it's an empty string.
        Document doc = Parser.parseBodyFragment(htmlFragment, "");

        // Assert: The parsed body's HTML should show that the unclosed tag was correctly handled and closed.
        Element body = doc.body();
        assertEquals(expectedBodyHtml, body.html());
    }
}