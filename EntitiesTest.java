package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTest {
    // Test data containing special characters
    private static final String TEST_STRING = "Hello &<> Å å π 新 there ¾ © » ' \"";
    private static final String EMOJI_HUNDRED_POINTS = "\uD83D\uDCAF";

    /* Tests for basic escaping functionality */
    
    @Test
    public void escapeBaseModeAsciiCharset() {
        String escaped = Entities.escape(TEST_STRING, 
            new OutputSettings().charset("ascii").escapeMode(base));
        
        assertEquals("Hello &amp;&lt;&gt; &Aring; &aring; &#x3c0; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", 
            escaped);
    }

    @Test
    public void escapeExtendedModeAsciiCharset() {
        String escaped = Entities.escape(TEST_STRING, 
            new OutputSettings().charset("ascii").escapeMode(extended));
        
        assertEquals("Hello &amp;&lt;&gt; &angst; &aring; &pi; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", 
            escaped);
    }

    @Test
    public void escapeXhtmlModeAsciiCharset() {
        String escaped = Entities.escape(TEST_STRING, 
            new OutputSettings().charset("ascii").escapeMode(xhtml));
        
        assertEquals("Hello &amp;&lt;&gt; &#xc5; &#xe5; &#x3c0; &#x65b0; there &#xbe; &#xa9; &#xbb; &#x27; &quot;", 
            escaped);
    }

    @Test
    public void escapeExtendedModeUtfCharset() {
        String escaped = Entities.escape(TEST_STRING, 
            new OutputSettings().charset("UTF-8").escapeMode(extended));
        
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escaped);
    }

    @Test
    public void escapeXhtmlModeUtfCharset() {
        String escaped = Entities.escape(TEST_STRING, 
            new OutputSettings().charset("UTF-8").escapeMode(xhtml));
        
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &#x27; &quot;", escaped);
    }

    @Test
    public void roundtripUnescapeAfterEscape() {
        OutputSettings[] settings = {
            new OutputSettings().charset("ascii").escapeMode(base),
            new OutputSettings().charset("ascii").escapeMode(extended),
            new OutputSettings().charset("ascii").escapeMode(xhtml),
            new OutputSettings().charset("UTF-8").escapeMode(extended),
            new OutputSettings().charset("UTF-8").escapeMode(xhtml)
        };

        for (OutputSettings setting : settings) {
            String escaped = Entities.escape(TEST_STRING, setting);
            assertEquals(TEST_STRING, Entities.unescape(escaped));
        }
    }

    @Test 
    public void escapeWithDefaultSettings() {
        String escaped = Entities.escape(TEST_STRING);
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escaped);
    }

    /* Tests for supplementary/emoji characters */
    
    @Test
    public void escapeSupplementaryCharacterBaseMode() {
        String text = "\uD835\uDD59"; // Mathematical double-struck small h
        String escaped = Entities.escape(text, 
            new OutputSettings().charset("ascii").escapeMode(base));
        assertEquals("&#x1d559;", escaped);
    }

    @Test
    public void escapeSupplementaryCharacterExtendedMode() {
        String text = "\uD835\uDD59";
        String escaped = Entities.escape(text, 
            new OutputSettings().charset("ascii").escapeMode(extended));
        assertEquals("&hopf;", escaped);
    }

    @Test
    public void escapeSupplementaryCharacterUtfCharset() {
        String text = "\uD835\uDD59";
        String escaped = Entities.escape(text, 
            new OutputSettings().charset("UTF-8").escapeMode(extended));
        assertEquals(text, escaped);
    }

    @Test
    public void escapeHighValueSupplementaryCharacter() {
        String text = new String(Character.toChars(135361)); // 𡃁 (U+210C1)
        String escapedAscii = Entities.escape(text, 
            new OutputSettings().charset("ascii").escapeMode(base));
        assertEquals("&#x210c1;", escapedAscii);
        
        String escapedUtf = Entities.escape(text, 
            new OutputSettings().charset("UTF-8").escapeMode(base));
        assertEquals(text, escapedUtf);
    }

    /* Tests for multi-character entities */
    
    @Test
    public void unescapeMultiCharacterEntities() {
        String text = "&NestedGreaterGreater; &nGg; &nGt; &nGtv; &Gt; &gg;";
        String expected = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
        assertEquals(expected, Entities.unescape(text));
    }

    @Test
    public void escapeMultiCharacterEntities() {
        String text = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
        String escaped = Entities.escape(text, 
            new OutputSettings().charset("ascii").escapeMode(extended));
        
        assertEquals("&Gt; &Gg;&#x338; &Gt;&#x20d2; &Gt;&#x338; &Gt; &Gt;", escaped);
        assertEquals(text, Entities.unescape(escaped));
    }

    /* Tests for XHTML specific entities */
    
    @Test
    public void xhtmlEntityCodepointLookup() {
        assertEquals(38, xhtml.codepointForName("amp"));
        assertEquals(62, xhtml.codepointForName("gt"));
        assertEquals(60, xhtml.codepointForName("lt"));
        assertEquals(34, xhtml.codepointForName("quot"));
    }

    @Test
    public void xhtmlEntityNameLookup() {
        assertEquals("amp", xhtml.nameForCodepoint(38));
        assertEquals("gt", xhtml.nameForCodepoint(62));
        assertEquals("lt", xhtml.nameForCodepoint(60));
        assertEquals("quot", xhtml.nameForCodepoint(34));
    }

    /* Tests for entity lookup by name */
    
    @Test
    public void getEntityByName() {
        assertEquals("≫⃒", Entities.getByName("nGt"));
        assertEquals("fj", Entities.getByName("fjlig"));
        assertEquals("≫", Entities.getByName("gg"));
        assertEquals("©", Entities.getByName("copy"));
    }

    /* Tests for unescape edge cases */
    
    @Test
    public void unescapeWithMixedEntities() {
        String text = "Hello &AElig; &amp;&LT&gt; &reg &angst; &angst &#960; &#960 &#x65B0; there &! &frac34; &copy; &COPY;";
        assertEquals("Hello Æ &<> ® Å &angst π π 新 there &! ¾ © ©", Entities.unescape(text));
    }

    @Test
    public void unescapeInvalidEntityReferences() {
        String text = "&0987654321; &unknown";
        assertEquals(text, Entities.unescape(text));
    }

    @Test
    public void strictUnescapeRequiresSemicolon() {
        String text = "Hello &amp= &amp;";
        assertEquals("Hello &amp= &", Entities.unescape(text, true));
    }

    @Test
    public void lenientUnescapeWithoutSemicolon() {
        String text = "Hello &amp= &amp;";
        assertEquals("Hello &= &", Entities.unescape(text));
        assertEquals("Hello &= &", Entities.unescape(text, false));
    }

    @Test
    public void unescapeWithPrefixMatch() {
        String text = "I'm &notit; I tell you. I'm &notin; I tell you.";
        assertEquals("I'm ¬it; I tell you. I'm ∉ I tell you.", Entities.unescape(text, false));
        assertEquals("I'm &notit; I tell you. I'm ∉ I tell you.", Entities.unescape(text, true));
    }

    /* Tests for case sensitivity */
    
    @Test
    public void escapeRespectsCase() {
        String text = "Ü ü & &";
        String escaped = Entities.escape(text, 
            new OutputSettings().charset("ascii").escapeMode(extended));
        assertEquals("&Uuml; &uuml; &amp; &amp;", escaped);
    }

    @Test
    public void unescapeRespectsCase() {
        String text = "&Uuml; &uuml; &amp; &AMP";
        assertEquals("Ü ü & &", Entities.unescape(text));
    }

    /* Tests for special character handling */
    
    @Test
    public void unescapeQuoteReplacements() {
        assertEquals("\\ $", Entities.unescape("&#92; &#36;"));
    }

    @Test
    public void letterDigitEntitiesInHtml() {
        String html = "<p>&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;</p>";
        Document doc = Jsoup.parse(html);
        
        // ASCII output should keep entities
        doc.outputSettings().charset("ascii");
        assertEquals("&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;", doc.select("p").first().html());
        assertEquals("¹²³¼½¾", doc.select("p").first().text());
        
        // UTF-8 output should decode entities
        doc.outputSettings().charset("UTF-8");
        assertEquals("¹²³¼½¾", doc.select("p").first().html());
    }

    @Test
    public void noAccidentalDecodingInUrls() {
        String url = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";
        assertEquals(url, Entities.unescape(url));
    }

    /* Tests for attribute escaping rules */
    
    @Test
    public void alwaysEscapeLtGtInAttributes() {
        String html = "<a title='<p>One</p>'>One</a>";
        Document doc = Jsoup.parse(html);
        Element link = doc.select("a").first();

        // Both base and xhtml modes should escape < and > in attributes
        doc.outputSettings().escapeMode(base);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", link.outerHtml());

        doc.outputSettings().escapeMode(xhtml);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", link.outerHtml());
    }

    /* Tests for control character handling */
    
    @Test
    public void controlCharactersEscapedInHtml() {
        String input = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";
        Document doc = Jsoup.parse(input);
        assertEquals(input, doc.body().html());
    }

    @Test
    public void controlCharactersRemovedInXml() {
        String input = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";
        Document xml = Jsoup.parse(input, "", Parser.xmlParser());
        assertEquals("<a foo=\"escbell\">Text  </a>", xml.html());
    }

    /* Tests for cloning behavior */
    
    @Test
    public void escapeWithClonedOutputSettings() {
        OutputSettings original = new OutputSettings();
        String text = "Hello &<> Å å π 新 there ¾ © »";
        
        OutputSettings clone1 = original.clone();
        OutputSettings clone2 = original.clone();

        String escaped1 = assertDoesNotThrow(() -> Entities.escape(text, clone1));
        String escaped2 = assertDoesNotThrow(() -> Entities.escape(text, clone2));
        assertEquals(escaped1, escaped2);
    }

    /* Tests for emoji handling */
    
    @Test
    void unescapeMultipointEncodedEmoji() {
        String emoji = Parser.unescapeEntities("&#55357;&#56495;", false);
        assertEquals(EMOJI_HUNDRED_POINTS, emoji);
    }

    @Test
    void unescapeSinglepointEncodedEmoji() {
        String emoji = Parser.unescapeEntities("&#128175;", false);
        assertEquals(EMOJI_HUNDRED_POINTS, emoji);
    }
}