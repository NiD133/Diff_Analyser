package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.*;
import java.nio.*;
import org.evosuite.runtime.*;
import org.evosuite.runtime.mock.java.io.*;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Entities_ESTest extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCodepointForName_AmpInXhtmlMode() throws Throwable {
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        int codepoint = xhtmlMode.codepointForName("amp");
        assertEquals(38, codepoint);
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithSpecificOutputSettings() throws Throwable {
        Document.OutputSettings settings = new Document.OutputSettings();
        QuietAppendable appendable = QuietAppendable.wrap(new MockPrintStream("regFX{u"));
        Entities.escape(appendable, "sup1$w1b#6@>wd6L", settings, 88);
        assertEquals(Document.OutputSettings.Syntax.html, settings.syntax());
    }

    @Test(timeout = 4000)
    public void testUnescapeStringWithNoEntities() {
        String result = Entities.unescape("FX{u", true);
        assertEquals("FX{u", result);
    }

    @Test(timeout = 4000)
    public void testUnescapeEmptyStringWithStrictFalse() {
        String result = Entities.unescape("", false);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testUnescapeEmptyString() {
        String result = Entities.unescape("");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testEscapeNullString() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String result = Entities.escape(null, settings);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testCodepointsForDegEntity() {
        int[] codepoints = new int[5];
        Entities.codepointsForName("deg", codepoints);
        assertArrayEquals(new int[]{176, 0, 0, 0, 0}, codepoints);
    }

    @Test(timeout = 4000)
    public void testUnescapeNullStringThrowsException() {
        assertThrows(NullPointerException.class, () -> Entities.unescape(null));
    }

    @Test(timeout = 4000)
    public void testIsNamedEntityWithNullThrowsException() {
        assertThrows(NullPointerException.class, () -> Entities.isNamedEntity(null));
    }

    @Test(timeout = 4000)
    public void testIsBaseNamedEntityWithNullThrowsException() {
        assertThrows(NullPointerException.class, () -> Entities.isBaseNamedEntity(null));
    }

    @Test(timeout = 4000)
    public void testGetByNameWithNullThrowsException() {
        assertThrows(NullPointerException.class, () -> Entities.getByName(null));
    }

    @Test(timeout = 4000)
    public void testFindPrefixWithNullThrowsException() {
        assertThrows(NullPointerException.class, () -> Entities.findPrefix(null));
    }

    @Test(timeout = 4000)
    public void testEscapeWithPipedWriterThrowsException() throws Throwable {
        PipedWriter writer = new PipedWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        assertThrows(RuntimeException.class, () -> 
            Entities.escape(appendable, "ai\"~k6zS*y qshCo<", settings, 373)
        );
    }

    @Test(timeout = 4000)
    public void testEscapeToReadOnlyBufferThrowsException() {
        CharBuffer buffer = CharBuffer.wrap("http://www.w3.org/000/svg").asReadOnlyBuffer();
        QuietAppendable appendable = QuietAppendable.wrap(buffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        assertThrows(ReadOnlyBufferException.class, () -> 
            Entities.escape(appendable, "http://www.w3.org/000/svg", settings, Integer.MAX_VALUE - 2)
        );
    }

    @Test(timeout = 4000)
    public void testEscapeToSmallBufferThrowsException() {
        CharBuffer buffer = CharBuffer.allocate(2);
        QuietAppendable appendable = QuietAppendable.wrap(buffer);
        Document.OutputSettings settings = new Document.OutputSettings();
        assertThrows(BufferOverflowException.class, () -> 
            Entities.escape(appendable, "[%s=%s]", settings, 2)
        );
    }

    @Test(timeout = 4000)
    public void testEscapeWithNullSettingsThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            Entities.escape("EEyJ~~*yFz>", null)
        );
    }

    @Test(timeout = 4000)
    public void testCodepointsForNameWithSmallArrayThrowsException() {
        int[] codepoints = new int[0];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            Entities.codepointsForName("amp", codepoints)
        );
    }

    @Test(timeout = 4000)
    public void testCodepointsForNameWithNullArrayThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            Entities.codepointsForName("nbsp", null)
        );
    }

    @Test(timeout = 4000)
    public void testIsBaseNamedEntityForValidEntity() {
        assertTrue(Entities.isBaseNamedEntity("not"));
    }

    @Test(timeout = 4000)
    public void testUnescapeNullWithStrictFalseThrowsException() {
        assertThrows(NullPointerException.class, () -> Entities.unescape(null, false));
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithDoubleQuote() {
        String escaped = Entities.escape("rNMdgQN,%1L1O-D\"");
        assertEquals("rNMdgQN,%1L1O-D&quot;", escaped);
    }

    @Test(timeout = 4000)
    public void testCoreCharsetByNameForNonStandard() {
        Entities.CoreCharset.byName("\"PvE5H.,d+SC ,Q,}'xM");
    }

    @Test(timeout = 4000)
    public void testEscapeWithAsciiCharset() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.charset("US-ASCII");
        String escaped = Entities.escape("US-ASCII", settings);
        assertEquals("US-ASCII", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeQuotesInXhtmlMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        String escaped = Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", settings);
        assertEquals("&quot;PvE5H.,d+SC ,Q,}&#x27;xM", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeInExtendedModeWithXmlSyntax() {
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml)
               .escapeMode(Entities.EscapeMode.extended);
        Entities.escape(appendable, "s{otKya)T<'/ETl/L", settings, 2694);
        assertEquals("s{otKya)T&lt;'/ETl/L", writer.toString());
    }

    @Test(timeout = 4000)
    public void testEscapeLessThanInXmlSyntax() throws Throwable {
        MockFileWriter fileWriter = new MockFileWriter("_rY1mF&]3c.e6+ D#w");
        QuietAppendable appendable = QuietAppendable.wrap(fileWriter);
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);
        Entities.escape(appendable, "l#C$31bf_{ww<5", settings, 34);
        assertTrue(settings.prettyPrint());
    }

    @Test(timeout = 4000)
    public void testEscapeNbspInXhtmlMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        String escaped = Entities.escape("yen\u00A0", settings);
        assertEquals("yen&#xa0;", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeQuotesInBaseMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String escaped = Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", settings);
        assertEquals("&quot;PvE5H.,d+SC ,Q,}&apos;xM", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeWithNullAppendableThrowsException() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertThrows(NullPointerException.class, () -> 
            Entities.escape(null, "></", settings, 108)
        );
    }

    @Test(timeout = 4000)
    public void testEscapeNbspInBaseMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        String escaped = Entities.escape("yen\u00A0", settings);
        assertEquals("yen&nbsp;", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeAmpersandAndGreaterThanInXmlSyntax() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);
        String escaped = Entities.escape("&c\n/*]]>*/", settings);
        assertEquals("&amp;c\n/*]]&gt;*/", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeToFile() throws Throwable {
        File tempFile = MockFile.createTempFile("sIXVxioN'm:", "ScriptDataDoubleEscapeEnd");
        MockPrintStream printStream = new MockPrintStream(tempFile);
        QuietAppendable appendable = QuietAppendable.wrap(printStream);
        Document.OutputSettings settings = new Document.OutputSettings();
        Entities.escape(appendable, "nQgZ:cx{U Z", settings, 732);
        assertEquals(11L, tempFile.length());
    }

    @Test(timeout = 4000)
    public void testEscapeStringOfSpaces() {
        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings settings = new Document.OutputSettings();
        Entities.escape(appendable, "                  ", settings, 1157);
        assertEquals(" ", writer.toString());
    }

    @Test(timeout = 4000)
    public void testEscapeWithNegativeCodepointThrowsException() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertThrows(NullPointerException.class, () -> 
            Entities.escape(null, " 08i,\"/*n8mN?7&B}vN#", settings, -2324)
        );
    }

    @Test(timeout = 4000)
    public void testEscapeLessThanWithNullAppendableThrowsException() {
        Document.OutputSettings settings = new Document.OutputSettings();
        assertThrows(NullPointerException.class, () -> 
            Entities.escape(null, "<^i1dEbS!x9O\nc", settings, 3276)
        );
    }

    @Test(timeout = 4000)
    public void testEscapeNullStringDefault() {
        String result = Entities.escape(null);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testFindPrefixForKnownEntity() {
        String prefix = Entities.findPrefix("ETH");
        assertEquals("ETH", prefix);
    }

    @Test(timeout = 4000)
    public void testFindPrefixForUnknownEntity() {
        String prefix = Entities.findPrefix("!2o1k(DYq");
        assertEquals("", prefix);
    }

    @Test(timeout = 4000)
    public void testGetByNameForEmptyString() {
        String entity = Entities.getByName("");
        assertNotNull(entity);
        assertEquals("", entity);
    }

    @Test(timeout = 4000)
    public void testGetByNameForNbspEntity() {
        String entity = Entities.getByName("nbsp");
        assertEquals("\u00A0", entity);
        assertNotNull(entity);
    }

    @Test(timeout = 4000)
    public void testIsBaseNamedEntityForUnknownEntity() {
        assertFalse(Entities.isBaseNamedEntity("InTemplate"));
    }

    @Test(timeout = 4000)
    public void testIsNamedEntityForExtendedEntity() {
        assertTrue(Entities.isNamedEntity("Racute"));
    }

    @Test(timeout = 4000)
    public void testIsNamedEntityForUnknownEntity() {
        assertFalse(Entities.isNamedEntity("US-ASCII"));
    }

    @Test(timeout = 4000)
    public void testNameForCodepointQuoteInBaseMode() {
        Entities.EscapeMode baseMode = Entities.EscapeMode.base;
        String name = baseMode.nameForCodepoint(34);
        assertEquals("quot", name);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointRacuteInExtendedMode() {
        Entities.EscapeMode extendedMode = Entities.EscapeMode.extended;
        String name = extendedMode.nameForCodepoint(340);
        assertEquals("Racute", name);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointGtInXhtmlMode() {
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        String name = xhtmlMode.nameForCodepoint(62);
        assertEquals("gt", name);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointTabReturnsEmpty() {
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        String name = xhtmlMode.nameForCodepoint(9);
        assertEquals("", name);
    }

    @Test(timeout = 4000)
    public void testCodepointsForNameForUnknownEntity() {
        int[] codepoints = new int[1];
        int count = Entities.codepointsForName("US-ASCII", codepoints);
        assertEquals(0, count);
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithCdata() {
        String escaped = Entities.escape("/*<![CDATA[*/\n");
        assertEquals("/*&lt;![CDATA[*/\n", escaped);
    }

    @Test(timeout = 4000)
    public void testEscapeQuotesToFile() throws Throwable {
        MockFile file = new MockFile("\"PvE5H.,d+SC ,Q,}'xM", "\"PvE5H.,d+SC ,Q,}'xM");
        MockFileWriter fileWriter = new MockFileWriter(file);
        QuietAppendable appendable = QuietAppendable.wrap(fileWriter);
        Document.OutputSettings settings = new Document.OutputSettings();
        Entities.escape(appendable, "\"PvE5H.,d+SC ,Q,}'xM", settings, 824);
        assertEquals(30, settings.maxPaddingWidth());
    }

    @Test(timeout = 4000)
    public void testUnescapeEntityWithoutSemicolon() {
        String unescaped = Entities.unescape("uml&quot;");
        assertEquals("uml\"", unescaped);
    }
}