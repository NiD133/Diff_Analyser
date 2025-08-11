package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for org.jsoup.nodes.Entities, focused on readability and intent.
 * 
 * Conventions used:
 * - Given / When / Then style comments where it aids understanding
 * - Helper methods to reduce noise around OutputSettings creation
 * - Descriptive test names that state behavior under test
 */
public class EntitiesTest {

    // Common sample strings used across tests
    private static final String SAMPLE = "Hello &<> Å å π 新 there ¾ © » ' \"";
    private static final String URL_WITH_AMPS = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";

    // Helper to build output settings succinctly
    private static OutputSettings out(String charset, Entities.EscapeMode mode) {
        return new OutputSettings().charset(charset).escapeMode(mode);
    }

    @Nested
    class Escaping {

        @Test
        void escapesRespectCharsetAndMode_andAreRoundTripSafe() {
            // Given
            String text = SAMPLE;

            // When
            String escapedAsciiBase     = Entities.escape(text, out("ascii", base));
            String escapedAsciiExtended = Entities.escape(text, out("ascii", extended));
            String escapedAsciiXhtml    = Entities.escape(text, out("ascii", xhtml));
            String escapedUtfExtended   = Entities.escape(text, out("UTF-8", extended));
            String escapedUtfXhtml      = Entities.escape(text, out("UTF-8", xhtml));

            // Then
            // Note: In base mode Å maps to &Aring;, but in extended it's &angst; (historical oddity)
            assertEquals("Hello &amp;&lt;&gt; &Aring; &aring; &#x3c0; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", escapedAsciiBase);
            assertEquals("Hello &amp;&lt;&gt; &angst; &aring; &pi; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", escapedAsciiExtended);
            assertEquals("Hello &amp;&lt;&gt; &#xc5; &#xe5; &#x3c0; &#x65b0; there &#xbe; &#xa9; &#xbb; &#x27; &quot;", escapedAsciiXhtml);
            assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escapedUtfExtended);
            assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &#x27; &quot;", escapedUtfXhtml);

            // And all forms unescape back to the original
            assertEquals(text, Entities.unescape(escapedAsciiBase));
            assertEquals(text, Entities.unescape(escapedAsciiExtended));
            assertEquals(text, Entities.unescape(escapedAsciiXhtml));
            assertEquals(text, Entities.unescape(escapedUtfExtended));
            assertEquals(text, Entities.unescape(escapedUtfXhtml));
        }

        @Test
        void escapeDefaults_useUtf8AndBaseMode() {
            // When
            String escaped = Entities.escape(SAMPLE);

            // Then
            assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escaped);
        }

        @Test
        void escapesSupplementaryCharactersCorrectly() {
            // Given: 𝕙 (U+1D559) as a surrogate pair
            String s = "\uD835\uDD59";

            // When / Then: ASCII + base -> numeric
            assertEquals("&#x1d559;", Entities.escape(s, out("ascii", base)));

            // When / Then: ASCII + extended -> named entity
            assertEquals("&hopf;", Entities.escape(s, out("ascii", extended)));

            // When / Then: UTF-8 + extended -> pass-through
            assertEquals(s, Entities.escape(s, out("UTF-8", extended)));
        }

        @Test
        void escapesSingleSupplementaryCodePoint() {
            // Given: arbitrary supplementary code point U+210C1
            String s = new String(Character.toChars(135361));

            // When / Then
            assertEquals("&#x210c1;", Entities.escape(s, out("ascii", base)));
            assertEquals(s, Entities.escape(s, out("UTF-8", base)));
        }

        @Test
        void alwaysEscapesLtAndGtInsideAttributeValues() {
            // Given
            String docHtml = "<a title='<p>One</p>'>One</a>";
            Document doc = Jsoup.parse(docHtml);
            Element a = doc.selectFirst("a");

            // When / Then: In both base and xhtml modes, < and > are escaped in attribute values
            doc.outputSettings().escapeMode(base);
            assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", a.outerHtml());

            doc.outputSettings().escapeMode(xhtml);
            assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", a.outerHtml());
        }

        @Test
        void controlCharactersAreEscapedInHtml_andRemovedInXml() {
            // Given control characters ESC (0x1B) and BEL (0x07) in text and attribute
            String input = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";

            // When / Then: HTML keeps them escaped for legibility
            Document html = Jsoup.parse(input);
            assertEquals(input, html.body().html());

            // When / Then: XML removes them
            Document xml = Jsoup.parse(input, "", Parser.xmlParser());
            assertEquals("<a foo=\"escbell\">Text  </a>", xml.html());
        }

        @Test
        void escapeUsesClonedOutputSettingsIndependently() {
            // Given
            OutputSettings baseSettings = new OutputSettings();
            String text = "Hello &<> Å å π 新 there ¾ © »";
            OutputSettings clone1 = baseSettings.clone();
            OutputSettings clone2 = baseSettings.clone();

            // When / Then: both clones are safe to use and produce same result
            String escaped1 = assertDoesNotThrow(() -> Entities.escape(text, clone1));
            String escaped2 = assertDoesNotThrow(() -> Entities.escape(text, clone2));
            assertEquals(escaped1, escaped2);
        }
    }

    @Nested
    class Unescaping {

        @Test
        void unescapesGeneralCases_andLeavesUnknowns() {
            // Given includes mixed cases, missing semicolons, numeric decimal and hex
            String input = "Hello &AElig; &amp;&LT&gt; &reg &angst; &angst &#960; &#960 &#x65B0; there &! &frac34; &copy; &COPY;";

            // When / Then
            assertEquals("Hello Æ &<> ® Å &angst π π 新 there &! ¾ © ©", Entities.unescape(input));

            // Unknown or invalid entities are preserved
            assertEquals("&0987654321; &unknown", Entities.unescape("&0987654321; &unknown"));
        }

        @Test
        void strictModeRequiresSemicolon_inAttributes() {
            // Given: "&amp=" should not decode in strict mode
            String input = "Hello &amp= &amp;";

            // When / Then
            assertEquals("Hello &amp= &", Entities.unescape(input, true));  // strict
            assertEquals("Hello &= &", Entities.unescape(input));           // default non-strict
            assertEquals("Hello &= &", Entities.unescape(input, false));    // explicit non-strict
        }

        @Test
        void doesNotDecodeBareAmpersandsInUrls() {
            assertEquals(URL_WITH_AMPS, Entities.unescape(URL_WITH_AMPS));
        }

        @Test
        void handlesQuoteAndBackslashReplacements() {
            String escaped = "&#92; &#36;"; // backslash and dollar
            String unescaped = "\\ $";
            assertEquals(unescaped, Entities.unescape(escaped));
        }

        @Test
        void correctlyResolvesPrefixMatches_perHtmlSpec() {
            // From WHATWG spec character-reference-state example:
            String input = "I'm &notit; I tell you. I'm &notin; I tell you.";

            // Non-strict will resolve &not; and leave 'it;' as text
            assertEquals("I'm ¬it; I tell you. I'm ∉ I tell you.", Entities.unescape(input, false));

            // Strict (attribute-style) leaves &notit; intact but resolves &notin;
            assertEquals("I'm &notit; I tell you. I'm ∉ I tell you.", Entities.unescape(input, true));
        }
    }

    @Nested
    class NamedEntities {

        @Test
        void xhtmlHasOnlyCoreEntities() {
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
        void getByNameReturnsExpectedCharacters() {
            assertEquals("≫⃒", Entities.getByName("nGt"));   // multi-codepoint
            assertEquals("fj", Entities.getByName("fjlig")); // ligature
            assertEquals("≫", Entities.getByName("gg"));
            assertEquals("©", Entities.getByName("copy"));
        }

        @Test
        void unescapesMultiCodepointEntities_withoutConflicts() {
            // gg is a single-point entity that could conflict with longer names like NestedGreaterGreater
            String entities = "&NestedGreaterGreater; &nGg; &nGt; &nGtv; &Gt; &gg;";
            String expected = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";

            assertEquals(expected, Entities.unescape(entities));

            String reEscaped = Entities.escape(expected, out("ascii", extended));
            assertEquals("&Gt; &Gg;&#x338; &Gt;&#x20d2; &Gt;&#x338; &Gt; &Gt;", reEscaped);

            // Round-trip safety
            assertEquals(expected, Entities.unescape(reEscaped));
        }

        @Test
        void supportsMultiCodepointAndSupplementalEntities() {
            // Multi-point
            assertEquals("\u2AFD\u20E5", Entities.unescape("&nparsl;"));

            // Supplemental plane
            assertEquals("⨔ \uD835\uDD2E", Entities.unescape("&npolint; &qfr;")); // 𝔮
        }

        @Test
        void caseSensitivity_rules() {
            // When escaping, case matters for named entities
            String unescaped = "Ü ü & &";
            assertEquals("&Uml; &uml; &amp; &amp;", Entities.escape(unescaped, out("ascii", base))); // base doesn't know Uml/uml; keep original behavior
            assertEquals("&Uuml; &uuml; &amp; &amp;", Entities.escape(unescaped, out("ascii", extended)));

            // When unescaping, standard case-insensitive handling with some tolerance for missing semicolon
            String escaped = "&Uuml; &uuml; &amp; &AMP";
            assertEquals("Ü ü & &", Entities.unescape(escaped));
        }
    }

    @Nested
    class IntegrationWithDocumentParsing {

        @Test
        void letterDigitEntities_roundTripThroughDocument() {
            String html = "<p>&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;</p>";
            Document doc = Jsoup.parse(html);

            // When: ASCII output -> entities preserved in HTML
            doc.outputSettings().charset("ascii");
            Element p = doc.selectFirst("p");
            assertEquals("&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;", p.html());
            assertEquals("¹²³¼½¾", p.text());

            // When: UTF-8 output -> actual characters in HTML
            doc.outputSettings().charset("UTF-8");
            assertEquals("¹²³¼½¾", p.html());
        }

        @Test
        void parsesHtmlEncodedEmoji_inBothForms() {
            // Multipoint surrogate pair
            String emojiMultipoint = Parser.unescapeEntities("&#55357;&#56495;", false); // 💯
            assertEquals("\uD83D\uDCAF", emojiMultipoint);

            // Single numeric code point
            String emoji = Parser.unescapeEntities("&#128175;", false); // 💯
            assertEquals("\uD83D\uDCAF", emoji);
        }
    }
}