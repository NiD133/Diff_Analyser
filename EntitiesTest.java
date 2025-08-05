package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitiesTest {
    
    // Test data constants for better readability
    private static final String MIXED_CHARACTERS_TEXT = "Hello &<> Å å π 新 there ¾ © » ' \"";
    private static final String SUPPLEMENTARY_CHARACTER = "\uD835\uDD59"; // Mathematical double-struck 'h'
    private static final String UNICODE_EMOJI = "\uD83D\uDCAF"; // 💯 emoji
    
    @Test 
    public void shouldEscapeTextWithDifferentCharsetAndEscapeModeConfigurations() {
        String inputText = MIXED_CHARACTERS_TEXT;
        
        // Test ASCII charset with different escape modes
        String asciiBaseEscaped = Entities.escape(inputText, createOutputSettings("ascii", base));
        String asciiExtendedEscaped = Entities.escape(inputText, createOutputSettings("ascii", extended));
        String asciiXhtmlEscaped = Entities.escape(inputText, createOutputSettings("ascii", xhtml));
        
        // Test UTF-8 charset with different escape modes
        String utf8ExtendedEscaped = Entities.escape(inputText, createOutputSettings("UTF-8", extended));
        String utf8XhtmlEscaped = Entities.escape(inputText, createOutputSettings("UTF-8", xhtml));

        // Verify ASCII base mode escaping
        assertEquals("Hello &amp;&lt;&gt; &Aring; &aring; &#x3c0; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", 
                asciiBaseEscaped);
        
        // Verify ASCII extended mode escaping (note: uses &angst; instead of &Aring;)
        assertEquals("Hello &amp;&lt;&gt; &angst; &aring; &pi; &#x65b0; there &frac34; &copy; &raquo; &apos; &quot;", 
                asciiExtendedEscaped);
        
        // Verify ASCII XHTML mode escaping (uses numeric entities only)
        assertEquals("Hello &amp;&lt;&gt; &#xc5; &#xe5; &#x3c0; &#x65b0; there &#xbe; &#xa9; &#xbb; &#x27; &quot;", 
                asciiXhtmlEscaped);
        
        // Verify UTF-8 extended mode (preserves Unicode characters, escapes quotes)
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", 
                utf8ExtendedEscaped);
        
        // Verify UTF-8 XHTML mode (similar to extended but uses numeric entity for apostrophe)
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &#x27; &quot;", 
                utf8XhtmlEscaped);

        // Verify round-trip conversion (escape then unescape should return original)
        assertRoundTripConversion(inputText, asciiBaseEscaped);
        assertRoundTripConversion(inputText, asciiExtendedEscaped);
        assertRoundTripConversion(inputText, asciiXhtmlEscaped);
        assertRoundTripConversion(inputText, utf8ExtendedEscaped);
        assertRoundTripConversion(inputText, utf8XhtmlEscaped);
    }

    @Test 
    public void shouldUseDefaultSettingsWhenEscapingWithoutConfiguration() {
        String inputText = MIXED_CHARACTERS_TEXT;
        String escapedWithDefaults = Entities.escape(inputText);
        
        // Default settings should be UTF-8 charset with extended escape mode
        assertEquals("Hello &amp;&lt;&gt; Å å π 新 there ¾ © » &apos; &quot;", escapedWithDefaults);
    }

    @Test 
    public void shouldEscapeSupplementaryUnicodeCharacters() {
        String supplementaryChar = SUPPLEMENTARY_CHARACTER; // Mathematical double-struck 'h'
        
        String asciiBaseEscaped = Entities.escape(supplementaryChar, createOutputSettings("ascii", base));
        String asciiExtendedEscaped = Entities.escape(supplementaryChar, createOutputSettings("ascii", extended));
        String utf8ExtendedEscaped = Entities.escape(supplementaryChar, createOutputSettings("UTF-8", extended));
        
        // ASCII base mode should use numeric entity
        assertEquals("&#x1d559;", asciiBaseEscaped);
        
        // ASCII extended mode should use named entity when available
        assertEquals("&hopf;", asciiExtendedEscaped);
        
        // UTF-8 should preserve the original character
        assertEquals(supplementaryChar, utf8ExtendedEscaped);
    }

    @Test 
    public void shouldUnescapeMultiCharacterEntities() {
        String entitiesText = "&NestedGreaterGreater; &nGg; &nGt; &nGtv; &Gt; &gg;";
        String expectedUnescaped = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
        
        // Test unescaping multi-character entities
        assertEquals(expectedUnescaped, Entities.unescape(entitiesText));
        
        // Test round-trip: unescape then escape should be reversible
        String reEscaped = Entities.escape(expectedUnescaped, createOutputSettings("ascii", extended));
        assertEquals("&Gt; &Gg;&#x338; &Gt;&#x20d2; &Gt;&#x338; &Gt; &Gt;", reEscaped);
        assertEquals(expectedUnescaped, Entities.unescape(reEscaped));
    }

    @Test 
    public void shouldProvideXhtmlEntityMappings() {
        // Test codepoint to name mappings for XHTML entities
        assertEquals(38, xhtml.codepointForName("amp"));   // &
        assertEquals(62, xhtml.codepointForName("gt"));    // >
        assertEquals(60, xhtml.codepointForName("lt"));    // <
        assertEquals(34, xhtml.codepointForName("quot"));  // "

        // Test name to codepoint mappings for XHTML entities
        assertEquals("amp", xhtml.nameForCodepoint(38));
        assertEquals("gt", xhtml.nameForCodepoint(62));
        assertEquals("lt", xhtml.nameForCodepoint(60));
        assertEquals("quot", xhtml.nameForCodepoint(34));
    }

    @Test 
    public void shouldRetrieveCharactersByEntityName() {
        assertEquals("≫⃒", Entities.getByName("nGt"));      // nested greater-than with vertical line
        assertEquals("fj", Entities.getByName("fjlig"));    // fj ligature
        assertEquals("≫", Entities.getByName("gg"));        // much greater-than
        assertEquals("©", Entities.getByName("copy"));      // copyright symbol
    }

    @Test 
    public void shouldEscapeSupplementaryCharactersCorrectly() {
        String supplementaryChar = new String(Character.toChars(135361)); // High codepoint character
        
        String asciiEscaped = Entities.escape(supplementaryChar, createOutputSettings("ascii", base));
        String utf8Escaped = Entities.escape(supplementaryChar, createOutputSettings("UTF-8", base));
        
        // ASCII should escape to numeric entity
        assertEquals("&#x210c1;", asciiEscaped);
        
        // UTF-8 should preserve original character
        assertEquals(supplementaryChar, utf8Escaped);
    }

    @Test 
    public void shouldHandleMultiCharacterEntityReferences() {
        String entityText = "&nparsl;";
        String expectedResult = "\u2AFD\u20E5"; // Parallel to and slanted equal
        
        assertEquals(expectedResult, Entities.unescape(entityText));
    }

    @Test 
    public void shouldHandleSupplementaryCharacterEntityReferences() {
        String entitiesText = "&npolint; &qfr;";
        String expectedResult = "⨔ \uD835\uDD2E"; // Line integration not including pole + mathematical fraktur q
        
        assertEquals(expectedResult, Entities.unescape(entitiesText));
    }

    @Test 
    public void shouldUnescapeVariousHtmlEntities() {
        String htmlEntities = "Hello &AElig; &amp;&LT&gt; &reg &angst; &angst &#960; &#960 &#x65B0; there &! &frac34; &copy; &COPY;";
        String expectedResult = "Hello Æ &<> ® Å &angst π π 新 there &! ¾ © ©";
        
        assertEquals(expectedResult, Entities.unescape(htmlEntities));

        // Test handling of invalid/unknown entities
        assertEquals("&0987654321; &unknown", Entities.unescape("&0987654321; &unknown"));
    }

    @Test 
    public void shouldHandleStrictUnescapingForAttributes() {
        String textWithIncompleteEntities = "Hello &amp= &amp;";
        
        // Strict mode (for attributes): requires proper entity termination
        assertEquals("Hello &amp= &", Entities.unescape(textWithIncompleteEntities, true));
        
        // Non-strict mode: more lenient parsing
        assertEquals("Hello &= &", Entities.unescape(textWithIncompleteEntities, false));
        assertEquals("Hello &= &", Entities.unescape(textWithIncompleteEntities)); // default is non-strict
    }

    @Test 
    public void shouldHandlePrefixMatchingInEntityNames() {
        // Test case from HTML specification for ambiguous entity prefixes
        String ambiguousEntities = "I'm &notit; I tell you. I'm &notin; I tell you.";
        String expectedNonStrict = "I'm ¬it; I tell you. I'm ∉ I tell you.";
        String expectedStrict = "I'm &notit; I tell you. I'm ∉ I tell you.";
        
        assertEquals(expectedNonStrict, Entities.unescape(ambiguousEntities, false));
        assertEquals(expectedStrict, Entities.unescape(ambiguousEntities, true));
    }

    @Test 
    public void shouldHandleCaseSensitiveEntities() {
        String unicodeText = "Ü ü & &";
        String expectedEscaped = "&Uuml; &uuml; &amp; &amp;";
        
        assertEquals(expectedEscaped, 
                Entities.escape(unicodeText, createOutputSettings("ascii", extended)));

        // Test case-insensitive unescaping
        String mixedCaseEntities = "&Uuml; &uuml; &amp; &AMP";
        assertEquals("Ü ü & &", Entities.unescape(mixedCaseEntities));
    }

    @Test 
    public void shouldUnescapeSpecialCharacters() {
        String numericEntities = "&#92; &#36;"; // backslash and dollar sign
        String expectedResult = "\\ $";

        assertEquals(expectedResult, Entities.unescape(numericEntities));
    }

    @Test 
    public void shouldHandleLetterDigitEntitiesInHtmlDocument() {
        String htmlWithEntities = "<p>&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;</p>";
        Document document = Jsoup.parse(htmlWithEntities);
        Element paragraph = document.select("p").first();
        
        // Test ASCII output (should preserve entities)
        document.outputSettings().charset("ascii");
        assertEquals("&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;", paragraph.html());
        assertEquals("¹²³¼½¾", paragraph.text());
        
        // Test UTF-8 output (should show actual characters)
        document.outputSettings().charset("UTF-8");
        assertEquals("¹²³¼½¾", paragraph.html());
    }

    @Test 
    public void shouldNotDecodeUrlParameters() {
        String urlWithParameters = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";
        
        // URL parameters containing & should not be treated as entities
        assertEquals(urlWithParameters, Entities.unescape(urlWithParameters));
    }

    @Test 
    public void shouldAlwaysEscapeLessThanAndGreaterThanInAttributes() {
        String htmlWithAttributeContent = "<a title='<p>One</p>'>One</a>";
        Document document = Jsoup.parse(htmlWithAttributeContent);
        Element anchor = document.select("a").first();

        // Both base and XHTML modes should escape < and > in attributes
        document.outputSettings().escapeMode(base);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", anchor.outerHtml());

        document.outputSettings().escapeMode(xhtml);
        assertEquals("<a title=\"&lt;p&gt;One&lt;/p&gt;\">One</a>", anchor.outerHtml());
    }

    @Test 
    public void shouldEscapeControlCharactersInHtml() {
        String htmlWithControlChars = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";
        
        // HTML parser should preserve control character entities
        Document htmlDocument = Jsoup.parse(htmlWithControlChars);
        assertEquals(htmlWithControlChars, htmlDocument.body().html());

        // XML parser should remove control characters
        Document xmlDocument = Jsoup.parse(htmlWithControlChars, "", Parser.xmlParser());
        assertEquals("<a foo=\"escbell\">Text  </a>", xmlDocument.html());
    }
    
    @Test 
    public void shouldEscapeUsingClonedOutputSettings() {
        OutputSettings originalSettings = new OutputSettings();
        String testText = "Hello &<> Å å π 新 there ¾ © »";
        
        OutputSettings clonedSettings1 = originalSettings.clone();
        OutputSettings clonedSettings2 = originalSettings.clone();

        // Both cloned settings should produce identical results
        String escaped1 = assertDoesNotThrow(() -> Entities.escape(testText, clonedSettings1));
        String escaped2 = assertDoesNotThrow(() -> Entities.escape(testText, clonedSettings2));
        assertEquals(escaped1, escaped2);
    }

    @Test 
    void shouldParseHtmlEncodedEmojiFromMultipleCodepoints() {
        String multiPointEmoji = Parser.unescapeEntities("&#55357;&#56495;", false); // 💯 as two surrogates
        assertEquals(UNICODE_EMOJI, multiPointEmoji);
    }

    @Test 
    void shouldParseHtmlEncodedEmojiFromSingleCodepoint() {
        String singlePointEmoji = Parser.unescapeEntities("&#128175;", false); // 💯 as single codepoint
        assertEquals(UNICODE_EMOJI, singlePointEmoji);
    }
    
    // Helper methods for better code reuse and readability
    
    private OutputSettings createOutputSettings(String charset, Entities.EscapeMode escapeMode) {
        return new OutputSettings().charset(charset).escapeMode(escapeMode);
    }
    
    private void assertRoundTripConversion(String original, String escaped) {
        assertEquals(original, Entities.unescape(escaped), 
                "Round-trip conversion failed for: " + escaped);
    }
}