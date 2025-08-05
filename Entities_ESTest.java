package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.jsoup.internal.QuietAppendable;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class Entities_ESTest extends Entities_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCodepointForAmpEntity() throws Throwable {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        int codepoint = escapeMode.codepointForName("amp");
        assertEquals(38, codepoint);
    }

    @Test(timeout = 4000)
    public void testEscapeWithQuietAppendable() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        MockPrintStream mockPrintStream = new MockPrintStream("regFX{u");
        QuietAppendable quietAppendable = QuietAppendable.wrap(mockPrintStream);
        Entities.escape(quietAppendable, "sup1$w1b#6@>wd6L", outputSettings, 88);
        assertEquals(Document.OutputSettings.Syntax.html, outputSettings.syntax());
    }

    @Test(timeout = 4000)
    public void testUnescapeWithoutChange() throws Throwable {
        String result = Entities.unescape("FX{u", true);
        assertEquals("FX{u", result);
    }

    @Test(timeout = 4000)
    public void testUnescapeEmptyString() throws Throwable {
        String result = Entities.unescape("", false);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testEscapeNullString() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String result = Entities.escape((String) null, outputSettings);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testCodepointsForDegEntity() throws Throwable {
        int[] codepoints = new int[5];
        Entities.codepointsForName("deg", codepoints);
        assertArrayEquals(new int[]{176, 0, 0, 0, 0}, codepoints);
    }

    @Test(timeout = 4000)
    public void testUnescapeNullStringThrowsException() throws Throwable {
        try {
            Entities.unescape((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIsNamedEntityNullThrowsException() throws Throwable {
        try {
            Entities.isNamedEntity((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIsBaseNamedEntityNullThrowsException() throws Throwable {
        try {
            Entities.isBaseNamedEntity((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetByNameNullThrowsException() throws Throwable {
        try {
            Entities.getByName((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFindPrefixNullThrowsException() throws Throwable {
        try {
            Entities.findPrefix((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEscapeWithUnconnectedPipeThrowsException() throws Throwable {
        PipedWriter pipedWriter = new PipedWriter();
        QuietAppendable quietAppendable = QuietAppendable.wrap(pipedWriter);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            Entities.escape(quietAppendable, "ai\"~k6zS*y qshCo<", outputSettings, 373);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.jsoup.internal.QuietAppendable$BaseAppendable", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeWithReadOnlyBufferThrowsException() throws Throwable {
        CharBuffer charBuffer = CharBuffer.wrap((CharSequence) "http://www.w3.org/000/svg");
        QuietAppendable quietAppendable = QuietAppendable.wrap(charBuffer);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            Entities.escape(quietAppendable, "http://www.w3.org/000/svg", outputSettings, Integer.MAX_VALUE);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.StringCharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeWithBufferOverflowThrowsException() throws Throwable {
        CharBuffer charBuffer = CharBuffer.allocate(2);
        QuietAppendable quietAppendable = QuietAppendable.wrap(charBuffer);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            Entities.escape(quietAppendable, "[%s=%s]", outputSettings, 2);
            fail("Expecting exception: BufferOverflowException");
        } catch (BufferOverflowException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeWithNullOutputSettingsThrowsException() throws Throwable {
        try {
            Entities.escape("EEyJ~~*yFz>", (Document.OutputSettings) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Entities", e);
        }
    }

    @Test(timeout = 4000)
    public void testCodepointsForNameWithEmptyArrayThrowsException() throws Throwable {
        int[] codepoints = new int[0];
        try {
            Entities.codepointsForName("amp", codepoints);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.jsoup.nodes.Entities", e);
        }
    }

    @Test(timeout = 4000)
    public void testCodepointsForNameWithNullArrayThrowsException() throws Throwable {
        try {
            Entities.codepointsForName("nbsp", (int[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Entities", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsBaseNamedEntityReturnsTrue() throws Throwable {
        boolean result = Entities.isBaseNamedEntity("not");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testUnescapeNullStringWithStrictThrowsException() throws Throwable {
        try {
            Entities.unescape((String) null, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEscapeStringWithQuotes() throws Throwable {
        String result = Entities.escape("rNMdgQN,%1L1O-D\"");
        assertEquals("rNMdgQN,%1L1O-D&quot;", result);
    }

    @Test(timeout = 4000)
    public void testCoreCharsetByName() throws Throwable {
        Entities.CoreCharset.byName("\"PvE5H.,d+SC ,Q,}'xM");
    }

    @Test(timeout = 4000)
    public void testEscapeWithUSASCIICharset() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset("US-ASCII");
        String result = Entities.escape("US-ASCII", outputSettings);
        assertEquals("US-ASCII", result);
    }

    @Test(timeout = 4000)
    public void testEscapeWithXhtmlEscapeMode() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Document.OutputSettings updatedSettings = outputSettings.escapeMode(escapeMode);
        String result = Entities.escape("\"PvE5H.,d+SC ,Q,}'xM", updatedSettings);
        assertEquals("&quot;PvE5H.,d+SC ,Q,}&#x27;xM", result);
    }

    @Test(timeout = 4000)
    public void testEscapeWithXmlSyntax() throws Throwable {
        StringWriter stringWriter = new StringWriter();
        QuietAppendable quietAppendable = QuietAppendable.wrap(stringWriter);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        Document.OutputSettings updatedSettings = outputSettings.syntax(syntax);
        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        Document.OutputSettings finalSettings = updatedSettings.escapeMode(escapeMode);
        Entities.escape(quietAppendable, "s{otKya)T<'/ETl/L", finalSettings, 2694);
        assertEquals("s{otKya)T&lt;'/ETl/L", stringWriter.toString());
    }

    @Test(timeout = 4000)
    public void testEscapeWithMockFileWriter() throws Throwable {
        MockFileWriter mockFileWriter = new MockFileWriter("_rY1mF&]3c.e6+ D#w");
        QuietAppendable quietAppendable = QuietAppendable.wrap(mockFileWriter);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        outputSettings.syntax(syntax);
        Entities.escape(quietAppendable, "l#C$31bf_{ww<5", outputSettings, 34);
        assertTrue(outputSettings.prettyPrint());
    }

    @Test(timeout = 4000)
    public void testEscapeWithXhtmlEscapeModeForNonBreakingSpace() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Document.OutputSettings updatedSettings = outputSettings.escapeMode(escapeMode);
        String result = Entities.escape("yen\u00A0", updatedSettings);
        assertEquals("yen&#xa0;", result);
    }

    @Test(timeout = 4000)
    public void testEscapeWithDefaultSettingsForNonBreakingSpace() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String result = Entities.escape("yen\u00A0", outputSettings);
        assertEquals("yen&nbsp;", result);
    }

    @Test(timeout = 4000)
    public void testEscapeWithXmlSyntaxAndCDATA() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        Document.OutputSettings updatedSettings = outputSettings.syntax(syntax);
        String result = Entities.escape("&c\n/*]]>*/", updatedSettings);
        assertEquals("&amp;c\n/*]]&gt;*/", result);
    }

    @Test(timeout = 4000)
    public void testEscapeWithMockPrintStream() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        MockFile file = MockFile.createTempFile("sIXVxioN'm:", "ScriptDataDoubleEscapeEnd");
        MockPrintStream mockPrintStream = new MockPrintStream(file);
        QuietAppendable quietAppendable = QuietAppendable.wrap(mockPrintStream);
        Entities.escape(quietAppendable, "nQgZ:cx{U Z", outputSettings, 732);
        assertEquals(11L, file.length());
    }

    @Test(timeout = 4000)
    public void testEscapeWithSpaces() throws Throwable {
        StringWriter stringWriter = new StringWriter();
        QuietAppendable quietAppendable = QuietAppendable.wrap(stringWriter);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Entities.escape(quietAppendable, "                  ", outputSettings, 1157);
        assertEquals(" ", stringWriter.toString());
    }

    @Test(timeout = 4000)
    public void testEscapeWithNullQuietAppendableThrowsException() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            Entities.escape((QuietAppendable) null, " 08i,\"/*n8mN?7&B}vN#", outputSettings, -2324);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEscapeWithNullQuietAppendableAndXmlSyntaxThrowsException() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            Entities.escape((QuietAppendable) null, "<^i1dEbS!x9O\nc", outputSettings, 3276);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Entities", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeNullStringWithDefaultSettings() throws Throwable {
        String result = Entities.escape((String) null);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testFindPrefixForETH() throws Throwable {
        String result = Entities.findPrefix("ETH");
        assertEquals("ETH", result);
    }

    @Test(timeout = 4000)
    public void testFindPrefixForNonEntity() throws Throwable {
        String result = Entities.findPrefix("!2o1k(DYq");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testGetByNameForEmptyString() throws Throwable {
        String result = Entities.getByName("");
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testGetByNameForNbsp() throws Throwable {
        String result = Entities.getByName("nbsp");
        assertEquals("\u00A0", result);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testIsBaseNamedEntityReturnsFalse() throws Throwable {
        boolean result = Entities.isBaseNamedEntity("InTemplate");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testIsNamedEntityReturnsTrue() throws Throwable {
        boolean result = Entities.isNamedEntity("Racute");
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testIsNamedEntityReturnsFalse() throws Throwable {
        boolean result = Entities.isNamedEntity("US-ASCII");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointInBaseEscapeMode() throws Throwable {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        String result = escapeMode.nameForCodepoint(34);
        assertEquals("quot", result);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointInExtendedEscapeMode() throws Throwable {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        String result = escapeMode.nameForCodepoint(340);
        assertEquals("Racute", result);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointInXhtmlEscapeMode() throws Throwable {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        String result = escapeMode.nameForCodepoint(62);
        assertEquals("gt", result);
    }

    @Test(timeout = 4000)
    public void testNameForCodepointNotFoundInXhtmlEscapeMode() throws Throwable {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        String result = escapeMode.nameForCodepoint(9);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testCodepointsForNameWithNonEntity() throws Throwable {
        int[] codepoints = new int[1];
        int result = Entities.codepointsForName("US-ASCII", codepoints);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testEscapeCData() throws Throwable {
        String result = Entities.escape("/*<![CDATA[*/\n");
        assertEquals("/*&lt;![CDATA[*/\n", result);
    }

    @Test(timeout = 4000)
    public void testEscapeWithMockFileWriterAndOutputSettings() throws Throwable {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        MockFile mockFile = new MockFile("\"PvE5H.,d+SC ,Q,}'xM", "\"PvE5H.,d+SC ,Q,}'xM");
        MockFileWriter mockFileWriter = new MockFileWriter(mockFile);
        QuietAppendable quietAppendable = QuietAppendable.wrap(mockFileWriter);
        Entities.escape(quietAppendable, "\"PvE5H.,d+SC ,Q,}'xM", outputSettings, 824);
        assertEquals(30, outputSettings.maxPaddingWidth());
    }

    @Test(timeout = 4000)
    public void testUnescapeWithUmlAndQuote() throws Throwable {
        String result = Entities.unescape("uml&quot;");
        assertEquals("uml\"", result);
    }
}