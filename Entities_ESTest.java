package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link Entities} class, focusing on its public API
 * for escaping, unescaping, and querying HTML entities.
 */
public class EntitiesTest {

    // --- Escape Tests ---

    /**
     * Tests the default HTML escaping, which should handle basic XML characters
     * plus named entities for apostrophes and non-breaking spaces.
     */
    @Test
    public void escape_inDefaultHtmlMode_shouldEscapeSpecialCharsAndNbsp() {
        String input = "Hello <>&\"'\u00A0World";
        String expected = "Hello &lt;&gt;&amp;&quot;&apos;&nbsp;World";

        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals(expected, Entities.escape(input, settings));
    }

    /**
     * In XML mode, only the essential characters (&, <, >, ") should be escaped.
     * Apostrophes should not be escaped.
     */
    @Test
    public void escape_inXmlMode_shouldEscapeSpecialCharsButNotApos() {
        String input = "Hello <>&\"'World";
        String expected = "Hello &lt;&gt;&amp;&quot;'World";

        Document.OutputSettings settings = new Document.OutputSettings()
                .syntax(OutputSettings.Syntax.xml);
        assertEquals(expected, Entities.escape(input, settings));
    }

    /**
     * In XHTML mode, special characters are escaped, and non-ASCII characters like
     * non-breaking spaces and apostrophes are escaped numerically.
     */
    @Test
    public void escape_inXhtmlMode_shouldEscapeSpecialCharsAndNbspNumerically() {
        String input = "Hello <>&\"'\u00A0World";
        String expected = "Hello &lt;&gt;&amp;&quot;&#x27;&#xa0;World";

        Document.OutputSettings settings = new Document.OutputSettings()
                .escapeMode(Entities.EscapeMode.xhtml);
        assertEquals(expected, Entities.escape(input, settings));
    }

    /**
     * The default escape() method should use the base HTML escape mode.
     */
    @Test
    public void escape_withDefaultSettings_shouldUseBaseHtmlEscaping() {
        String input = "My code is < 100 lines & it's \"great\"!";
        String expected = "My code is &lt; 100 lines &amp; it's &quot;great&quot;!";
        assertEquals(expected, Entities.escape(input));
    }

    /**
     * Characters that can be encoded by the output charset should not be escaped.
     */
    @Test
    public void escape_shouldNotEscapeCharactersInCharset() {
        String input = "Ascii-compatible characters: Hello 123";
        
        Document.OutputSettings settings = new Document.OutputSettings()
                .charset("US-ASCII");
        assertEquals(input, Entities.escape(input, settings));
    }

    @Test
    public void escape_withNullInput_shouldReturnEmptyString() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals("", Entities.escape(null, settings));
        assertEquals("", Entities.escape(null));
    }

    @Test
    public void escape_withEmptyInput_shouldReturnEmptyString() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertEquals("", Entities.escape("", settings));
        assertEquals("", Entities.escape(""));
    }

    // --- Unescape Tests ---

    @Test
    public void unescape_shouldUnescapeBaseAndExtendedEntities() {
        String input = "Hello &lt;&gt;&amp;&quot;&apos;&nbsp;&#x212B;World"; // x212B is the Angstrom sign
        String expected = "Hello <>&\"'\u00A0\u212BWorld";
        assertEquals(expected, Entities.unescape(input));
    }



    @Test
    public void unescape_withNoEntities_shouldReturnOriginalString() {
        String input = "This string has no entities.";
        assertEquals(input, Entities.unescape(input));
    }

    @Test
    public void unescape_withEmptyInput_shouldReturnEmptyString() {
        assertEquals("", Entities.unescape(""));
    }

    // --- Entity Information & Query Tests ---

    @Test
    public void isNamedEntity_shouldDifferentiateKnownAndUnknownEntities() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("Racute")); // An extended entity
        assertFalse(Entities.isNamedEntity("notAnEntity"));
    }

    @Test
    public void isBaseNamedEntity_shouldDifferentiateBaseAndExtendedEntities() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("Racute")); // Extended, not base
        assertFalse(Entities.isBaseNamedEntity("notAnEntity"));
    }

    @Test
    public void getByName_shouldReturnCharacterForKnownEntity() {
        assertEquals("\u00A0", Entities.getByName("nbsp"));
        assertEquals(">", Entities.getByName("gt"));
    }

    @Test
    public void getByName_shouldReturnEmptyStringForUnknownEntity() {
        assertEquals("", Entities.getByName("notAnEntity"));
        assertEquals("", Entities.getByName(""));
    }

    @Test
    public void findPrefix_shouldReturnLongestMatchingPrefix() {
        assertEquals("ETH", Entities.findPrefix("ETHical"));
    }

    @Test
    public void findPrefix_shouldReturnEmptyStringForNoMatch() {
        assertEquals("", Entities.findPrefix("noMatchHere"));
    }

    // --- Codepoints Tests ---

    @Test
    public void codepointsForName_shouldPopulateArrayForValidEntity() {
        int[] codepoints = new int[2];
        int count = Entities.codepointsForName("deg", codepoints);
        assertEquals(1, count);
        assertArrayEquals(new int[]{176, 0}, codepoints);
    }

    @Test
    public void codepointsForName_shouldReturnZeroForInvalidEntity() {
        int[] codepoints = new int[2];
        int count = Entities.codepointsForName("notAnEntity", codepoints);
        assertEquals(0, count);
    }

    // --- EscapeMode Enum Tests ---

    @Test
    public void escapeMode_codepointForName_shouldReturnCorrectValue() {
        assertEquals(38, Entities.EscapeMode.xhtml.codepointForName("amp"));
        assertEquals(62, Entities.EscapeMode.base.codepointForName("gt"));
        assertEquals(340, Entities.EscapeMode.extended.codepointForName("Racute"));
    }

    @Test
    public void escapeMode_nameForCodepoint_shouldReturnCorrectName() {
        assertEquals("quot", Entities.EscapeMode.base.nameForCodepoint(34));
        assertEquals("gt", Entities.EscapeMode.xhtml.nameForCodepoint(62));
        assertEquals("Racute", Entities.EscapeMode.extended.nameForCodepoint(340));
        assertEquals("", Entities.EscapeMode.xhtml.nameForCodepoint(9)); // Tab has no entity name
    }

    // --- Exception Handling Tests ---

    @Test(expected = NullPointerException.class)
    public void unescape_withNullInput_shouldThrowException() {
        Entities.unescape(null);
    }

    @Test(expected = NullPointerException.class)
    public void escape_withNullSettings_shouldThrowException() {
        Entities.escape("test", null);
    }

    @Test(expected = NullPointerException.class)
    public void isNamedEntity_withNullInput_shouldThrowException() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void isBaseNamedEntity_withNullInput_shouldThrowException() {
        Entities.isBaseNamedEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void getByName_withNullInput_shouldThrowException() {
        Entities.getByName(null);
    }

    @Test(expected = NullPointerException.class)
    public void findPrefix_withNullInput_shouldThrowException() {
        Entities.findPrefix(null);
    }

    @Test(expected = NullPointerException.class)
    public void codepointsForName_withNullArray_shouldThrowException() {
        Entities.codepointsForName("amp", null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void codepointsForName_withInsufficientArray_shouldThrowException() {
        // "amp" has 1 codepoint, but the array has size 0.
        Entities.codepointsForName("amp", new int[0]);
    }
}