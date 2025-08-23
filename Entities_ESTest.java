package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for Entities behavior.
 *
 * Notes:
 * - Uses only public API unless a package-private method is explicitly called.
 * - Avoids EvoSuite scaffolding and mock types for clarity.
 * - Groups tests by feature: lookup, escaping, unescaping, and error handling.
 */
public class EntitiesTest {

    // ------------------------
    // Entity lookup functions
    // ------------------------

    @Test
    public void codepointForName_amp_inXhtmlMode() {
        Entities.EscapeMode mode = Entities.EscapeMode.xhtml;
        assertEquals(38, mode.codepointForName("amp")); // '&' => 38
    }

    @Test
    public void nameForCodepoint_quot_inBaseMode() {
        assertEquals("quot", Entities.EscapeMode.base.nameForCodepoint(34)); // '"' => 34
    }

    @Test
    public void nameForCodepoint_Racute_inExtendedMode() {
        assertEquals("Racute", Entities.EscapeMode.extended.nameForCodepoint(340));
    }

    @Test
    public void nameForCodepoint_gt_inXhtmlMode() {
        assertEquals("gt", Entities.EscapeMode.xhtml.nameForCodepoint(62)); // '>' => 62
    }

    @Test
    public void nameForCodepoint_tab_inXhtmlMode_returnsEmpty() {
        assertEquals("", Entities.EscapeMode.xhtml.nameForCodepoint(9)); // not named in xhtml
    }

    @Test
    public void isNamedEntity_trueForKnown() {
        assertTrue(Entities.isNamedEntity("Racute"));
    }

    @Test
    public void isNamedEntity_falseForUnknown() {
        assertFalse(Entities.isNamedEntity("US-ASCII"));
    }

    @Test
    public void isBaseNamedEntity_trueForKnownBaseEntity() {
        assertTrue(Entities.isBaseNamedEntity("not"));
    }

    @Test
    public void isBaseNamedEntity_falseForNonEntity() {
        assertFalse(Entities.isBaseNamedEntity("InTemplate"));
    }

    @Test
    public void getByName_emptyString_returnsEmptyString() {
        assertEquals("", Entities.getByName(""));
    }

    @Test
    public void getByName_nbsp_returnsNbsp() {
        assertEquals("\u00A0", Entities.getByName("nbsp"));
    }

    @Test
    public void codepointsForName_deg_writesFirstSlot() {
        int[] cp = new int[5];
        Entities.codepointsForName("deg", cp);
        assertArrayEquals(new int[] {176, 0, 0, 0, 0}, cp);
    }

    @Test
    public void codepointsForName_unknown_returnsZeroCount() {
        int[] cp = new int[1];
        int count = Entities.codepointsForName("US-ASCII", cp);
        assertEquals(0, count);
    }

    @Test
    public void findPrefix_exactMatch() {
        assertEquals("ETH", Entities.findPrefix("ETH"));
    }

    @Test
    public void findPrefix_noMatch_returnsEmpty() {
        assertEquals("", Entities.findPrefix("!2o1k(DYq"));
    }

    // ------------------------
    // Escaping
    // ------------------------

    @Test
    public void escape_default_escapesQuote() {
        assertEquals("rNMdgQN,%1L1O-D&quot;", Entities.escape("rNMdgQN,%1L1O-D\""));
    }

    @Test
    public void escape_default_doesNotChangeAsciiLetters() {
        assertEquals("US-ASCII", Entities.escape("US-ASCII"));
    }

    @Test
    public void escape_default_nbspBecomesNamedEntity() {
        OutputSettings out = new OutputSettings(); // default: HTML, base entities
        assertEquals("yen&nbsp;", Entities.escape("yen\u00A0", out));
    }

    @Test
    public void escape_xhtml_nbspBecomesNumericEntity() {
        OutputSettings out = new OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("yen&#xa0;", Entities.escape("yen\u00A0", out));
    }

    @Test
    public void escape_xhtml_aposBecomesNumeric() {
        OutputSettings out = new OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&quot;PvE5H.,d+SC ,Q,}&#x27;xM",
            Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", out));
    }

    @Test
    public void escape_default_aposBecomesNamed() {
        OutputSettings out = new OutputSettings(); // default: base
        assertEquals("&quot;PvE5H.,d+SC ,Q,}&apos;xM",
            Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", out));
    }

    @Test
    public void escape_xmlSyntax_escapesGtInCdataClose() {
        OutputSettings out = new OutputSettings().syntax(OutputSettings.Syntax.xml);
        assertEquals("&amp;c\n/*]]&gt;*/", Entities.escape("&c\n/*]]>*/", out));
    }

    @Test
    public void escape_cdataStartIsEscaped() {
        assertEquals("/*&lt;![CDATA[*/\n", Entities.escape("/*<![CDATA[*/\n"));
    }

    @Test
    public void escape_nullString_returnsEmpty() {
        OutputSettings out = new OutputSettings();
        assertEquals("", Entities.escape((String) null, out));
        assertEquals("", Entities.escape((String) null));
    }

    // ------------------------
    // Unescaping
    // ------------------------

    @Test
    public void unescape_returnsInputWhenNoEntities() {
        assertEquals("FX{u", Entities.unescape("FX{u"));
    }

    @Test
    public void unescape_empty_returnsEmpty() {
        assertEquals("", Entities.unescape(""));
    }

    @Test
    public void unescape_namedEntity_quote() {
        assertEquals("uml\"", Entities.unescape("uml&quot;"));
    }

    // ------------------------
    // Error handling (nulls and invalid inputs)
    // ------------------------

    @Test(expected = NullPointerException.class)
    public void unescape_null_throwsNpe() {
        Entities.unescape((String) null);
    }

    // package-private overload; this test must be in the same package
    @Test(expected = NullPointerException.class)
    public void unescape_strict_null_throwsNpe() {
        Entities.unescape((String) null, false);
    }

    @Test(expected = NullPointerException.class)
    public void isNamedEntity_null_throwsNpe() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void isBaseNamedEntity_null_throwsNpe() {
        Entities.isBaseNamedEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void getByName_null_throwsNpe() {
        Entities.getByName(null);
    }

    @Test(expected = NullPointerException.class)
    public void findPrefix_null_throwsNpe() {
        Entities.findPrefix(null);
    }

    @Test(expected = NullPointerException.class)
    public void escape_nullOutputSettings_throwsNpe() {
        Entities.escape("EEyJ~~*yFz>", (OutputSettings) null);
    }

    @Test(expected = NullPointerException.class)
    public void codepointsForName_nullArray_throwsNpe() {
        Entities.codepointsForName("nbsp", null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void codepointsForName_emptyArray_throwsAioobe() {
        Entities.codepointsForName("amp", new int[0]);
    }
}