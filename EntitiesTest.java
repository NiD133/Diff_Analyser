package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A test suite for the Entities class, focusing on the clarity and maintainability of tests
 * for HTML and XML entity escaping and unescaping.
 */
public class EntitiesTest {

    /**
     * Tests the behavior of HTML entity escaping with an ASCII charset and 'base' escape mode.
     * In this mode, characters not in the ASCII set are escaped using their named or numeric entities from the base set.
     */
    @Test
    public void escapeWithAsciiAndBaseMode() {
        // Arrange
        String text = "Hello &<> Å å π 新 there ¾ © » ' \"";
        OutputSettings settings = new OutputSettings().charset("ascii").escapeMode(base);
        String expected = "Hello &<> Å å π 新 there ¾ © » ' "";

        // Act
        String escapedText = Entities.escape(text, settings);

        // Assert
        assertEquals(expected, escapedText);
    }

    /**
     * Tests escaping with an ASCII charset and 'extended' escape mode.
     * This mode uses a more comprehensive set of named entities compared to the 'base' mode.
     * For example, 'Å' is escaped as 'Å' instead of 'Å'.
     */
    @Test
    public void escapeWithAsciiAndExtendedMode() {
        // Arrange
        String text = "Hello &<> Å å π 新 there ¾ © » ' \"";
        OutputSettings settings = new OutputSettings().charset("ascii").escapeMode(extended);
        String expected = "Hello &<> Å å π 新 there ¾ © » ' "";

        // Act
        String escapedText = Entities.escape(text, settings);

        // Assert
        assertEquals(expected, escapedText);
    }

    /**
     * Tests escaping with an ASCII charset and 'xhtml' escape mode.
     * In XHTML mode, only the essential XML entities (&, <, >, ") are named. All other
     * characters outside the charset are escaped numerically.
     */
    @Test
    public void escapeWithAsciiAndXhtmlMode() {
        // Arrange
        String text = "Hello &<> Å å π 新 there ¾ © » ' \"";
        OutputSettings settings = new OutputSettings().charset("ascii").escapeMode(xhtml);
        // Note: The single quote (') is escaped as a numeric entity ''' in XHTML mode.
        String expected = "Hello &<> Å å π 新 there ¾ © » ' "";

        // Act
        String escapedText = Entities.escape(text, settings);

        // Assert
        assertEquals(expected, escapedText);
    }

    /**
     * Tests that when using a UTF-8 charset, characters supported by the charset are not escaped.
     * Only core HTML entities like '&', '<', '>', and '"' are escaped.
     */
    @Test
    public void escapeWithUtf8DoesNotEncodeSupportedCharacters() {
        // Arrange
        String text = "Hello &<> Å å π 新 there ¾ © » ' \"";
        OutputSettings utfExtendedSettings = new OutputSettings().charset("UTF-8").escapeMode(extended);
        OutputSettings utfXhtmlSettings = new OutputSettings().charset("UTF-8").escapeMode(xhtml);

        String expectedExtended = "Hello &<> Å å π 新 there ¾ © » ' "";
        String expectedXhtml = "Hello &<> Å å π 新 there ¾ © » ' "";

        // Act
        String escapedExtended = Entities.escape(text, utfExtendedSettings);
        String escapedXhtml = Entities.escape(text, utfXhtmlSettings);

        // Assert
        assertEquals(expectedExtended, escapedExtended);
        assertEquals(expectedXhtml, escapedXhtml);
    }

    /**
     * Verifies that escaping a string and then unescaping it results in the original string.
     * This test covers various escape modes and character sets to ensure a lossless roundtrip.
     */
    @Test
    public void escapeAndUnescapeRoundtripPreservesOriginalString() {
        // Arrange
        String originalText = "Hello &<> Å å π 新 there ¾ © » ' \"";
        OutputSettings asciiBase = new OutputSettings().charset("ascii").escapeMode(base);
        OutputSettings asciiExtended = new OutputSettings().charset("ascii").escapeMode(extended);
        OutputSettings asciiXhtml = new OutputSettings().charset("ascii").escapeMode(xhtml);
        OutputSettings utf8Extended = new OutputSettings().charset("UTF-8").escapeMode(extended);
        OutputSettings utf8Xhtml = new OutputSettings().charset("UTF-8").escapeMode(xhtml);

        // Act
        String escapedAsciiBase = Entities.escape(originalText, asciiBase);
        String escapedAsciiExtended = Entities.escape(originalText, asciiExtended);
        String escapedAsciiXhtml = Entities.escape(originalText, asciiXhtml);
        String escapedUtf8Extended = Entities.escape(originalText, utf8Extended);
        String escapedUtf8Xhtml = Entities.escape(originalText, utf8Xhtml);

        // Assert
        assertEquals(originalText, Entities.unescape(escapedAsciiBase));
        assertEquals(originalText, Entities.unescape(escapedAsciiExtended));
        assertEquals(originalText, Entities.unescape(escapedAsciiXhtml));
        assertEquals(originalText, Entities.unescape(escapedUtf8Extended));
        assertEquals(originalText, Entities.unescape(escapedUtf8Xhtml));
    }

    /**
     * Tests the default `escape` method, which should use UTF-8 and base escape mode.
     * This means non-ASCII characters are preserved, and only basic entities are used.
     */
    @Test
    public void escapeWithDefaultSettingsUsesUtf8AndBaseMode() {
        // Arrange
        String text = "Hello &<> Å å π 新 there ¾ © » ' \"";
        // Default output settings are UTF-8 with base escape mode. ' is escaped to '
        String expected = "Hello &<> Å å π 新 there ¾ © » ' "";

        // Act
        String escaped = Entities.escape(text);

        // Assert
        assertEquals(expected, escaped);
    }

    /**
     * Verifies that supplementary characters (those outside the Basic Multilingual Plane) are escaped correctly.
     */
    @Test
    public void escapeHandlesSupplementaryCharactersCorrectly() {
        // Arrange
        String mathSymbol = "\uD835\uDD59"; // Unicode character U+1D559
        String cjkSymbol = new String(Character.toChars(135361)); // Unicode character U+210C1
        OutputSettings asciiSettings = new OutputSettings().charset("ascii").escapeMode(base);
        OutputSettings asciiExtendedSettings = new OutputSettings().charset("ascii").escapeMode(extended);
        OutputSettings utf8Settings = new OutputSettings().charset("UTF-8").escapeMode(base);

        // Act & Assert for the mathematical symbol
        assertEquals("학", Entities.escape(mathSymbol, asciiSettings));
        assertEquals("𝕙", Entities.escape(mathSymbol, asciiExtendedSettings));
        assertEquals(mathSymbol, Entities.escape(mathSymbol, utf8Settings));

        // Act & Assert for the CJK symbol
        assertEquals("Ⴡ", Entities.escape(cjkSymbol, asciiSettings));
        assertEquals(cjkSymbol, Entities.escape(cjkSymbol, utf8Settings));
    }

    /**
     * Tests unescaping of entities that represent multiple characters and ensures the roundtrip (escape -> unescape) is correct.
     */
    @Test
    public void unescapeHandlesMultiCharacterEntitiesAndRoundtrips() {
        // Arrange
        // Input contains entities that could be misinterpreted as prefixes of others.
        String text = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
        String expectedUnescaped = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";
        OutputSettings settings = new OutputSettings().charset("ascii").escapeMode(extended);
        String expectedEscaped = "≫ ⋙̸ ≫⃒ ≫̸ ≫ ≫";

        // Act
        String unescaped = Entities.unescape(text);
        String escaped = Entities.escape(unescaped, settings);

        // Assert
        assertEquals(expectedUnescaped, unescaped);
        assertEquals(expectedEscaped, escaped);
        assertEquals(expectedUnescaped, Entities.unescape(escaped));
    }

    /**
     * Verifies that the 'xhtml' escape mode provides the correct codepoints and names for core XML entities.
     */
    @Test
    public void xhtmlModeProvidesCorrectCodepointsAndNames() {
        // Assert codepoint lookups
        assertEquals(38, xhtml.codepointForName("amp"));
        assertEquals(62, xhtml.codepointForName("gt"));
        assertEquals(60, xhtml.codepointForName("lt"));
        assertEquals(34, xhtml.codepointForName("quot"));

        // Assert name lookups
        assertEquals("amp", xhtml.nameForCodepoint(38));
        assertEquals("gt", xhtml.nameForCodepoint(62));
        assertEquals("lt", xhtml.nameForCodepoint(60));
        assertEquals("quot", xhtml.nameForCodepoint(34));
    }

    /**
     * Verifies that `getByName` correctly retrieves the character(s) for a given named entity.
     */
    @Test
    public void getByNameRetrievesCorrectCharacterForEntity() {
        // Arrange & Act & Assert
        assertEquals("≫⃒", Entities.getByName("nGt"));
        assertEquals("fj", Entities.getByName("fjlig"));
        assertEquals("≫", Entities.getByName("gg"));
        assertEquals("©", Entities.getByName("copy"));
    }

    /**
     * Tests a specific case where an entity represents multiple Unicode codepoints.
     */
    @Test
    public void unescapeHandlesMultiCodepointEntity() {
        // Arrange
        String text = "⫽⃥";
        String expected = "\u2AFD\u20E5";

        // Act & Assert
        assertEquals(expected, Entities.unescape(text));
    }

    /**
     * Tests unescaping of entities that represent supplementary characters.
     */
    @Test
    public void unescapeHandlesSupplementaryCharacterEntities() {
        // Arrange
        String text = "⨔ 𝔮";
        String expected = "⨔ \uD835\uDD2E"; // 𝔮

        // Act & Assert
        assertEquals(expected, Entities.unescape(text));
    }

    /**
     * Verifies that the unescape method correctly handles a mix of valid, partially valid, and invalid entities.
     */
    @Test
    public void unescapeHandlesMixedAndInvalidEntities() {
        // Arrange
        String text = "Hello Æ &<> ® Å &angst π π 新 there &! ¾ © ©";
        String expected = "Hello Æ &<> ® Å &angst π π 新 there &! ¾ © ©";

        // Act & Assert
        assertEquals(expected, Entities.unescape(text));
        assertEquals("&0987654321; &unknown", Entities.unescape("&0987654321; &unknown"));
    }

    /**
     * Verifies that unescaping in strict mode requires a terminating semicolon for named entities.
     * This is typically used for attribute values.
     */
    @Test
    public void unescapeInStrictModeRequiresTerminatingSemicolon() {
        // Arrange
        String text = "Hello &= &";

        // Act & Assert
        assertEquals("Hello &= &", Entities.unescape(text, true)); // Strict mode: &= is not unescaped
        assertEquals("Hello &= &", Entities.unescape(text, false)); // Non-strict mode
        assertEquals("Hello &= &", Entities.unescape(text)); // Default is non-strict
    }

    /**
     * Verifies unescaping of ambiguous entities where one name is a prefix of another.
     * See whatwg spec: https://html.spec.whatwg.org/multipage/parsing.html#character-reference-state
     */
    @Test
    public void unescapeHandlesAmbiguousPrefixEntitiesCorrectly() {
        // Arrange
        String text = "I'm ¬it; I tell you. I'm ∉ I tell you.";

        // Act & Assert
        // In non-strict (HTML text) mode, '¬it;' is not a valid entity, so it's left as is.
        assertEquals("I'm ¬it; I tell you. I'm ∉ I tell you.", Entities.unescape(text, false));

        // In strict (attribute) mode, '¬it;' is also not valid because it's not a complete match.
        assertEquals("I'm ¬it; I tell you. I'm ∉ I tell you.", Entities.unescape(text, true));
    }

    /**
     * Verifies that entity names are case-sensitive during escaping but case-insensitive during unescaping.
     */
    @Test
    public void escapeIsCaseSensitiveAndUnescapeIsCaseInsensitive() {
        // Arrange
        String unescaped = "Ü ü & &";
        String escapedWithCase = "Ü ü & &";
        String mixedCaseToUnescape = "Ü ü & &"; // & is not a valid entity name but should be unescaped

        // Act & Assert for escaping
        assertEquals(escapedWithCase,
                Entities.escape(unescaped, new OutputSettings().charset("ascii").escapeMode(extended)));

        // Act & Assert for unescaping
        assertEquals("Ü ü & &", Entities.unescape(mixedCaseToUnescape));
    }

    /**
     * Tests that numeric entities for characters like backslash and dollar sign are unescaped correctly.
     */
    @Test
    public void unescapeHandlesNumericEntitiesForSpecialCharacters() {
        // Arrange
        String escaped = "\ $";
        String unescaped = "\\ $";

        // Act & Assert
        assertEquals(unescaped, Entities.unescape(escaped));
    }

    /**
     * Verifies that parsing HTML containing entities that look like letters and digits works as expected.
     */
    @Test
    public void htmlParsingCorrectlyHandlesLetterLikeEntities() {
        // Arrange
        String html = "<p>¹²³¼½¾</p>";
        Document doc = Jsoup.parse(html);
        Element p = doc.select("p").first();

        // Assert that when outputting as ASCII, the entities are preserved.
        doc.outputSettings().charset("ascii");
        assertEquals("¹²³¼½¾", p.html());
        assertEquals("¹²³¼½¾", p.text());

        // Assert that when outputting as UTF-8, the characters are rendered directly.
        doc.outputSettings().charset("UTF-8");
        assertEquals("¹²³¼½¾", p.html());
    }

    /**
     * Ensures that strings containing ampersands, such as URLs with query parameters, are not incorrectly decoded.
     */
    @Test
    public void unescapeAvoidsSpuriousDecodingOfURLs() {
        // Arrange
        String url = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";

        // Act & Assert
        assertEquals(url, Entities.unescape(url));
    }

    /**
     * Confirms that '<' and '>' characters are always escaped within attribute values, regardless of the escape mode.
     * This is critical to prevent HTML injection.
     */
    @Test
    public void lessThanAndGreaterThanAreAlwaysEscapedInAttributeValues() {
        // Arrange
        String docHtml = "<a title='<p>One</p>'>One</a>";
        Document doc = Jsoup.parse(docHtml);
        Element element = doc.select("a").first();
        String expectedHtml = "<a title=\"<p>One</p>\">One</a>";

        // Act & Assert for 'base' mode
        doc.outputSettings().escapeMode(base);
        assertEquals(expectedHtml, element.outerHtml());

        // Act & Assert for 'xhtml' mode
        doc.outputSettings().escapeMode(xhtml);
        assertEquals(expectedHtml, element.outerHtml());
    }

    /**
     * Verifies how control characters are handled. They should be escaped in HTML but removed from XML.
     */
    @Test
    public void escapeHandlesControlCharactersDifferentlyForHtmlAndXml() {
        // Arrange
        String input = "<a foo=\"escbell\">Text  </a>";

        // Act & Assert for HTML
        Document doc = Jsoup.parse(input);
        assertEquals(input, doc.body().html());

        // Act & Assert for XML
        Document xml = Jsoup.parse(input, "", Parser.xmlParser());
        assertEquals("<a foo=\"escbell\">Text  </a>", xml.html());
    }

    /**
     * Ensures that cloning OutputSettings does not affect the outcome of the escape operation,
     * confirming that the cloned settings are independent.
     */
    @Test
    public void escapeWithClonedOutputSettingsIsConsistent() {
        // Arrange
        OutputSettings originalSettings = new OutputSettings();
        String text = "Hello &<> Å å π 新 there ¾ © »";

        // Act
        OutputSettings clone1 = originalSettings.clone();
        OutputSettings clone2 = originalSettings.clone();
        String escaped1 = assertDoesNotThrow(() -> Entities.escape(text, clone1));
        String escaped2 = assertDoesNotThrow(() -> Entities.escape(text, clone2));

        // Assert
        assertEquals(escaped1, escaped2);
    }

    /**
     * Verifies that an emoji represented by a multi-point HTML entity is unescaped correctly.
     */
    @Test
    public void unescapeParsesMultiPointEmojiEntity() {
        // Arrange
        String emojiEntity = "💯"; // 💯
        String expectedEmoji = "\uD83D\uDCAF";

        // Act
        String unescaped = Parser.unescapeEntities(emojiEntity, false);

        // Assert
        assertEquals(expectedEmoji, unescaped);
    }

    /**
     * Verifies that an emoji represented by a single-point HTML entity is unescaped correctly.
     */
    @Test
    public void unescapeParsesSinglePointEmojiEntity() {
        // Arrange
        String emojiEntity = ""; // 💯
        String expectedEmoji = "\uD83D\uDCAF";

        // Act
        String unescaped = Parser.unescapeEntities(emojiEntity, false);

        // Assert
        assertEquals(expectedEmoji, unescaped);
    }
}