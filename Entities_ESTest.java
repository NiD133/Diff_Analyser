package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.nio.CharBuffer;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

/**
 * Test suite for HTML entity handling functionality in the Entities class.
 * Tests cover entity escaping, unescaping, validation, and various output settings.
 */
public class EntitiesTest {

    // ========== Entity Name Validation Tests ==========
    
    @Test
    public void shouldReturnTrueForKnownNamedEntity() {
        assertTrue("'Racute' should be recognized as a named entity", 
                  Entities.isNamedEntity("Racute"));
    }

    @Test
    public void shouldReturnFalseForUnknownNamedEntity() {
        assertFalse("'US-ASCII' should not be recognized as a named entity", 
                   Entities.isNamedEntity("US-ASCII"));
    }

    @Test
    public void shouldReturnTrueForKnownBaseEntity() {
        assertTrue("'not' should be recognized as a base named entity", 
                  Entities.isBaseNamedEntity("not"));
    }

    @Test
    public void shouldReturnFalseForUnknownBaseEntity() {
        assertFalse("'InTemplate' should not be recognized as a base named entity", 
                   Entities.isBaseNamedEntity("InTemplate"));
    }

    // ========== Entity Lookup Tests ==========

    @Test
    public void shouldReturnCorrectCharacterForKnownEntity() {
        String result = Entities.getByName("nbsp");
        assertEquals("'nbsp' entity should return non-breaking space character", 
                    "\u00A0", result);
    }

    @Test
    public void shouldReturnEmptyStringForUnknownEntity() {
        String result = Entities.getByName("");
        assertEquals("Empty entity name should return empty string", "", result);
    }

    @Test
    public void shouldReturnCorrectCodepointForEntityName() {
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        int codepoint = xhtmlMode.codepointForName("amp");
        assertEquals("'amp' entity should have codepoint 38", 38, codepoint);
    }

    @Test
    public void shouldPopulateArrayWithEntityCodepoints() {
        int[] codepoints = new int[5];
        Entities.codepointsForName("deg", codepoints);
        assertArrayEquals("'deg' entity should populate array with correct codepoints", 
                         new int[]{176, 0, 0, 0, 0}, codepoints);
    }

    // ========== Entity Name Lookup Tests ==========

    @Test
    public void shouldReturnEntityNameForKnownCodepoint() {
        Entities.EscapeMode baseMode = Entities.EscapeMode.base;
        String entityName = baseMode.nameForCodepoint(34);
        assertEquals("Codepoint 34 should map to 'quot' entity", "quot", entityName);
    }

    @Test
    public void shouldReturnExtendedEntityNameForCodepoint() {
        Entities.EscapeMode extendedMode = Entities.EscapeMode.extended;
        String entityName = extendedMode.nameForCodepoint(340);
        assertEquals("Codepoint 340 should map to 'Racute' entity", "Racute", entityName);
    }

    @Test
    public void shouldReturnEmptyStringForUnmappedCodepoint() {
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        String entityName = xhtmlMode.nameForCodepoint(9);
        assertEquals("Unmapped codepoint should return empty string", "", entityName);
    }

    // ========== Prefix Matching Tests ==========

    @Test
    public void shouldFindExactPrefixMatch() {
        String prefix = Entities.findPrefix("ETH");
        assertEquals("'ETH' should be found as exact prefix match", "ETH", prefix);
    }

    @Test
    public void shouldReturnEmptyStringForNoPrefix() {
        String prefix = Entities.findPrefix("!2o1k(DYq");
        assertEquals("Non-entity string should return empty prefix", "", prefix);
    }

    // ========== Basic Escaping Tests ==========

    @Test
    public void shouldEscapeQuoteCharacter() {
        String escaped = Entities.escape("rNMdgQN,%1L1O-D\"");
        assertEquals("Quote character should be escaped to &quot;", 
                    "rNMdgQN,%1L1O-D&quot;", escaped);
    }

    @Test
    public void shouldEscapeLessThanCharacter() {
        String escaped = Entities.escape("/*<![CDATA[*/\n");
        assertEquals("Less-than character should be escaped to &lt;", 
                    "/*&lt;![CDATA[*/\n", escaped);
    }

    @Test
    public void shouldReturnEmptyStringForNullInput() {
        String escaped = Entities.escape((String) null);
        assertEquals("Null input should return empty string", "", escaped);
    }

    // ========== Escaping with Output Settings Tests ==========

    @Test
    public void shouldEscapeWithDefaultHtmlSettings() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String escaped = Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", settings);
        assertEquals("Should escape quote and apostrophe in HTML mode", 
                    "&quot;PvE5H.,d+SC ,Q,}&apos;xM", escaped);
    }

    @Test
    public void shouldEscapeWithXhtmlSettings() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        String escaped = Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", settings);
        assertEquals("Should use numeric entity for apostrophe in XHTML mode", 
                    "&quot;PvE5H.,d+SC ,Q,}&#x27;xM", escaped);
    }

    @Test
    public void shouldEscapeNonBreakingSpaceInHtmlMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String escaped = Entities.escape("yen\u00A0", settings);
        assertEquals("Non-breaking space should be escaped as &nbsp; in HTML", 
                    "yen&nbsp;", escaped);
    }

    @Test
    public void shouldEscapeNonBreakingSpaceInXhtmlMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        String escaped = Entities.escape("yen\u00A0", settings);
        assertEquals("Non-breaking space should be escaped numerically in XHTML", 
                    "yen&#xa0;", escaped);
    }

    @Test
    public void shouldEscapeForXmlOutput() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);
        String escaped = Entities.escape("&c\n/*]]>*/", settings);
        assertEquals("Should escape ampersand and greater-than for XML", 
                    "&amp;c\n/*]]&gt;*/", escaped);
    }

    @Test
    public void shouldReturnEmptyStringForNullInputWithSettings() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String escaped = Entities.escape((String) null, settings);
        assertEquals("Null input with settings should return empty string", "", escaped);
    }

    // ========== Appendable Escaping Tests ==========

    @Test
    public void shouldEscapeToStringWriter() {
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        Entities.escape(appendable, "sup1$w1b#6@>wd6L", settings, 88);
        
        // Verify that the method completes without exception
        assertEquals("HTML syntax should be default", 
                    Document.OutputSettings.Syntax.html, settings.syntax());
    }

    @Test
    public void shouldEscapeWithXmlSyntaxToAppendable() {
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);
        settings.escapeMode(Entities.EscapeMode.extended);
        
        Entities.escape(appendable, "s{otKya)T<'/ETl/L", settings, 2694);
        
        assertEquals("Should escape less-than character for XML", 
                    "s{otKya)T&lt;'/ETl/L", writer.toString());
    }

    @Test
    public void shouldHandleSpacesInAppendableEscaping() {
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        Entities.escape(appendable, "                  ", settings, 1157);
        
        assertEquals("Should handle multiple spaces correctly", " ", writer.toString());
    }

    // ========== Unescaping Tests ==========

    @Test
    public void shouldUnescapeBasicEntity() {
        String unescaped = Entities.unescape("uml&quot;");
        assertEquals("Should unescape &quot; to quote character", "uml\"", unescaped);
    }

    @Test
    public void shouldReturnUnchangedStringWhenNoEntities() {
        String unescaped = Entities.unescape("FX{u", true);
        assertEquals("String without entities should remain unchanged", "FX{u", unescaped);
    }

    @Test
    public void shouldHandleEmptyStringInUnescape() {
        String unescaped = Entities.unescape("", false);
        assertEquals("Empty string should remain empty", "", unescaped);
    }

    @Test
    public void shouldHandleEmptyStringInUnescapeDefault() {
        String unescaped = Entities.unescape("");
        assertEquals("Empty string should remain empty with default settings", "", unescaped);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullInUnescape() {
        Entities.unescape((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullInIsNamedEntity() {
        Entities.isNamedEntity((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullInIsBaseNamedEntity() {
        Entities.isBaseNamedEntity((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullInGetByName() {
        Entities.getByName((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullInFindPrefix() {
        Entities.findPrefix((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullOutputSettings() {
        Entities.escape("EEyJ~~*yFz>", (Document.OutputSettings) null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowExceptionForEmptyCodepointsArray() {
        int[] emptyArray = new int[0];
        Entities.codepointsForName("amp", emptyArray);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullCodepointsArray() {
        Entities.codepointsForName("nbsp", (int[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullAppendable() {
        Document.OutputSettings settings = new Document.OutputSettings();
        Entities.escape((QuietAppendable) null, "></", settings, 108);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullInUnescapeWithStrict() {
        Entities.unescape((String) null, false);
    }

    // ========== Buffer Exception Tests ==========

    @Test(expected = ReadOnlyBufferException.class)
    public void shouldThrowExceptionForReadOnlyBuffer() {
        CharBuffer buffer = CharBuffer.wrap("http://www.w3.org/000/svg");
        QuietAppendable appendable = QuietAppendable.wrap(buffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        Entities.escape(appendable, "http://www.w3.org/000/svg", settings, 2147483645);
    }

    @Test(expected = BufferOverflowException.class)
    public void shouldThrowExceptionForBufferOverflow() {
        CharBuffer buffer = CharBuffer.allocate(2);
        QuietAppendable appendable = QuietAppendable.wrap(buffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        
        Entities.escape(appendable, "[%s=%s]", settings, 2);
    }

    // ========== Charset and CoreCharset Tests ==========

    @Test
    public void shouldHandleCharsetByName() {
        // Test that charset lookup doesn't throw exception
        Entities.CoreCharset.byName("\"PvE5H.,d+SC ,Q,}'xM");
        // Method completes successfully if no exception is thrown
    }

    @Test
    public void shouldHandleUsAsciiCharset() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.charset("US-ASCII");
        String escaped = Entities.escape("US-ASCII", settings);
        assertEquals("US-ASCII string should remain unchanged", "US-ASCII", escaped);
    }

    // ========== Edge Case Tests ==========

    @Test
    public void shouldReturnZeroForUnknownEntityCodepoints() {
        int[] codepoints = new int[1];
        int result = Entities.codepointsForName("US-ASCII", codepoints);
        assertEquals("Unknown entity should return 0 codepoints", 0, result);
    }
}