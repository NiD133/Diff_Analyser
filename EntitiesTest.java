package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTest {

    private static final String SAMPLE_TEXT = "Hello &<> Å å π 新 there ¾ © » ' \"";
    private static final String SUPPLEMENTARY_TEXT = "\uD835\uDD59";
    private static final String MULTI_CHAR_ENTITIES = "&NestedGreaterGreater; &nGg; &nGt; &nGtv; &Gt; &gg;";
    private static final String MULTI_CHAR_UNESCAPED = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
    private static final String XHTML_ENTITIES = "Hello &AElig; &amp;&LT&gt; &reg &angst; &angst &#960; &#960 &#x65B0; there &! &frac34; &copy; &COPY;";
    private static final String XHTML_UNESCAPED = "Hello Æ &<> ® Å &angst π π 新 there &! ¾ © ©";

    @Test
    public void testEscapeWithDifferentModes() {
        // Test escaping with different escape modes and charsets
        String escapedAscii = Entities.escape(SAMPLE_TEXT, new OutputSettings().charset("ascii").escapeMode(base));
        String escapedAsciiFull = Entities.escape(SAMPLE_TEXT, new OutputSettings().charset("ascii").escapeMode(extended));
        String escapedAsciiXhtml = Entities.escape(SAMPLE_TEXT, new OutputSettings().charset("ascii").escapeMode(xhtml));
        String escapedUtfFull = Entities.escape(SAMPLE_TEXT, new OutputSettings().charset("UTF-8").escapeMode(extended));
        String escapedUtfMin = Entities.escape(SAMPLE_TEXT, new OutputSettings().charset("UTF-8").escapeMode(xhtml));

        assertEquals("Hello &amp;&lt;&gt; &Aring; &aring; &#x3c0; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", escapedAscii);
        assertEquals("Hello &amp;&lt;&gt; &angst; &aring; &pi; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", escapedAsciiFull);
        assertEquals("Hello &amp;&lt;&gt; &#xc5; &#xe5; &#x3c0; &#x65b0; there &#xbe; &#xa9; &#xbb; &#x27; &quot;", escapedAsciiXhtml);
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escapedUtfFull);
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &#x27; &quot;", escapedUtfMin);

        // Round trip: ensure unescaping returns to original text
        assertEquals(SAMPLE_TEXT, Entities.unescape(escapedAscii));
        assertEquals(SAMPLE_TEXT, Entities.unescape(escapedAsciiFull));
        assertEquals(SAMPLE_TEXT, Entities.unescape(escapedAsciiXhtml));
        assertEquals(SAMPLE_TEXT, Entities.unescape(escapedUtfFull));
        assertEquals(SAMPLE_TEXT, Entities.unescape(escapedUtfMin));
    }

    @Test
    public void testEscapeWithDefaultSettings() {
        // Test escaping with default settings
        String escaped = Entities.escape(SAMPLE_TEXT);
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escaped);
    }

    @Test
    public void testEscapeSupplementaryCharacters() {
        // Test escaping supplementary characters
        String escapedAscii = Entities.escape(SUPPLEMENTARY_TEXT, new OutputSettings().charset("ascii").escapeMode(base));
        assertEquals("&#x1d559;", escapedAscii);

        String escapedAsciiFull = Entities.escape(SUPPLEMENTARY_TEXT, new OutputSettings().charset("ascii").escapeMode(extended));
        assertEquals("&hopf;", escapedAsciiFull);

        String escapedUtf = Entities.escape(SUPPLEMENTARY_TEXT, new OutputSettings().charset("UTF-8").escapeMode(extended));
        assertEquals(SUPPLEMENTARY_TEXT, escapedUtf);
    }

    @Test
    public void testUnescapeMultiCharacterEntities() {
        // Test unescaping multi-character entities
        assertEquals(MULTI_CHAR_UNESCAPED, Entities.unescape(MULTI_CHAR_ENTITIES));

        String escaped = Entities.escape(MULTI_CHAR_UNESCAPED, new OutputSettings().charset("ascii").escapeMode(extended));
        assertEquals("&Gt; &Gg;&#x338; &Gt;&#x20d2; &Gt;&#x338; &Gt; &Gt;", escaped);

        assertEquals(MULTI_CHAR_UNESCAPED, Entities.unescape(escaped));
    }

    @Test
    public void testXhtmlEntities() {
        // Test XHTML entities encoding and decoding
        assertEquals(38, xhtml.codepointForName("amp"));
        assertEquals(62, xhtml.codepointForName("gt"));
        assertEquals(60, xhtml.codepointForName("lt"));
        assertEquals(34, xhtml.codepointForName("quot"));

        assertEquals("amp", xhtml.nameForCodepoint(38));
        assertEquals("gt", xhtml.nameForCodepoint(62));
        assertEquals("lt", xhtml.nameForCodepoint(60));
        assertEquals("quot", xhtml.nameForCodepoint(34));
    }

    @Test
    public void testGetByName() {
        // Test getting characters by entity name
        assertEquals("≫⃒", Entities.getByName("nGt"));
        assertEquals("fj", Entities.getByName("fjlig"));
        assertEquals("≫", Entities.getByName("gg"));
        assertEquals("©", Entities.getByName("copy"));
    }

    @Test
    public void testEscapeSupplementaryCharacter() {
        // Test escaping a supplementary character
        String text = new String(Character.toChars(135361));
        String escapedAscii = Entities.escape(text, new OutputSettings().charset("ascii").escapeMode(base));
        assertEquals("&#x210c1;", escapedAscii);

        String escapedUtf = Entities.escape(text, new OutputSettings().charset("UTF-8").escapeMode(base));
        assertEquals(text, escapedUtf);
    }

    @Test
    public void testNotMissingMultiCharacterEntities() {
        // Test unescaping multi-character entities that are not missing
        String text = "&nparsl;";
        String unescaped = "\u2AFD\u20E5";
        assertEquals(unescaped, Entities.unescape(text));
    }

    @Test
    public void testNotMissingSupplementaryEntities() {
        // Test unescaping supplementary entities that are not missing
        String text = "&npolint; &qfr;";
        String unescaped = "⨔ \uD835\uDD2E"; // 𝔮
        assertEquals(unescaped, Entities.unescape(text));
    }

    @Test
    public void testUnescapeXhtmlEntities() {
        // Test unescaping XHTML entities
        assertEquals(XHTML_UNESCAPED, Entities.unescape(XHTML_ENTITIES));
        assertEquals("&0987654321; &unknown", Entities.unescape("&0987654321; &unknown"));
    }

    @Test
    public void testStrictUnescape() {
        // Test strict unescaping for attributes
        String text = "Hello &amp= &amp;";
        assertEquals("Hello &amp= &", Entities.unescape(text, true));
        assertEquals("Hello &= &", Entities.unescape(text));
        assertEquals("Hello &= &", Entities.unescape(text, false));
    }

    @Test
    public void testPrefixMatch() {
        // Test prefix matching for entities
        String text = "I'm &notit; I tell you. I'm &notin; I tell you.";
        assertEquals("I'm ¬it; I tell you. I'm ∉ I tell you.", Entities.unescape(text, false));
        assertEquals("I'm &notit; I tell you. I'm ∉ I tell you.", Entities.unescape(text, true)); // not for attributes
    }

    @Test
    public void testCaseSensitiveEntities() {
        // Test case sensitivity in entities
        String unescaped = "Ü ü & &";
        assertEquals("&Uuml; &uuml; &amp; &amp;", Entities.escape(unescaped, new OutputSettings().charset("ascii").escapeMode(extended)));

        String escaped = "&Uuml; &uuml; &amp; &AMP";
        assertEquals("Ü ü & &", Entities.unescape(escaped));
    }

    @Test
    public void testQuoteReplacements() {
        // Test quote replacements
        String escaped = "&#92; &#36;";
        String unescaped = "\\ $";
        assertEquals(unescaped, Entities.unescape(escaped));
    }

    @Test
    public void testLetterDigitEntities() {
        // Test letter and digit entities
        String html = "<p>&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;</p>";
        Document doc = Jsoup.parse(html);
        doc.outputSettings().charset("ascii");
        Element p = doc.select("p").first();
        assertEquals("&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;", p.html());
        assertEquals("¹²³¼½¾", p.text());
        doc.outputSettings().charset("UTF-8");
        assertEquals("¹²³¼½¾", p.html());
    }

    @Test
    public void testNoSpuriousDecodes() {
        // Test that no spurious decodes occur
        String string = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";
        assertEquals(string, Entities.unescape(string));
    }

    @Test
    public void testAlwaysEscapeLtAndGtInAttributeValues() {
        // Test that < and > are always escaped in attribute values
        String docHtml = "<a title='<p>One</p>'>One</a>";
        Document doc = Jsoup.parse(docHtml);
        Element element = doc.select("a").first();

        doc.outputSettings().escapeMode(base);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", element.outerHtml());

        doc.outputSettings().escapeMode(xhtml);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", element.outerHtml());
    }

    @Test
    public void testControlCharactersAreEscaped() {
        // Test that control characters are escaped
        String input = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";
        Document doc = Jsoup.parse(input);
        assertEquals(input, doc.body().html());

        Document xml = Jsoup.parse(input, "", Parser.xmlParser());
        assertEquals("<a foo=\"escbell\">Text  </a>", xml.html());
    }

    @Test
    public void testEscapeByClonedOutputSettings() {
        // Test escaping with cloned output settings
        OutputSettings outputSettings = new OutputSettings();
        OutputSettings clone1 = outputSettings.clone();
        OutputSettings clone2 = outputSettings.clone();

        String escaped1 = assertDoesNotThrow(() -> Entities.escape(SAMPLE_TEXT, clone1));
        String escaped2 = assertDoesNotThrow(() -> Entities.escape(SAMPLE_TEXT, clone2));
        assertEquals(escaped1, escaped2);
    }

    @Test
    public void testParseHtmlEncodedEmojiMultipoint() {
        // Test parsing HTML encoded emoji with multipoint
        String emoji = Parser.unescapeEntities("&#55357;&#56495;", false); // 💯
        assertEquals("\uD83D\uDCAF", emoji);
    }

    @Test
    public void testParseHtmlEncodedEmoji() {
        // Test parsing HTML encoded emoji
        String emoji = Parser.unescapeEntities("&#128175;", false); // 💯
        assertEquals("\uD83D\uDCAF", emoji);
    }
}