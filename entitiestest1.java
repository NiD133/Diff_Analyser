package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTestTest1 {

    @Test
    public void escape() {
        // escape is maximal (as in the escapes cover use in both text and attributes; vs Element.html() which checks if attribute or text and minimises escapes
        String text = "Hello &<> Å å π 新 there ¾ © » ' \"";
        String escapedAscii = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(base));
        String escapedAsciiFull = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(extended));
        String escapedAsciiXhtml = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(xhtml));
        String escapedUtfFull = Entities.escape(text, new OutputSettings().charset("UTF-8").escapeMode(extended));
        String escapedUtfMin = Entities.escape(text, new OutputSettings().charset("UTF-8").escapeMode(xhtml));
        assertEquals("Hello &amp;&lt;&gt; &Aring; &aring; &#x3c0; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", escapedAscii);
        assertEquals("Hello &amp;&lt;&gt; &angst; &aring; &pi; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", escapedAsciiFull);
        assertEquals("Hello &amp;&lt;&gt; &#xc5; &#xe5; &#x3c0; &#x65b0; there &#xbe; &#xa9; &#xbb; &#x27; &quot;", escapedAsciiXhtml);
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escapedUtfFull);
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &#x27; &quot;", escapedUtfMin);
        // odd that it's defined as aring in base but angst in full
        // round trip
        assertEquals(text, Entities.unescape(escapedAscii));
        assertEquals(text, Entities.unescape(escapedAsciiFull));
        assertEquals(text, Entities.unescape(escapedAsciiXhtml));
        assertEquals(text, Entities.unescape(escapedUtfFull));
        assertEquals(text, Entities.unescape(escapedUtfMin));
    }
}
