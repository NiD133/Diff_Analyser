package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class NumericEntityUnescaper_ESTest extends NumericEntityUnescaper_ESTest_scaffolding {

    private static final NumericEntityUnescaper.OPTION[] EMPTY_OPTIONS = new NumericEntityUnescaper.OPTION[0];

    @Test(timeout = 4000)
    public void testTranslateIncompleteEntity() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'\u0000', '&', '#', 'y', '\u0000', '\u0000', '\u0000', '\u0000'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        unescaper.translate(charBuffer, writer);

        assertEquals("\u0000&#y\u0000\u0000\u0000\u0000", writer.toString());
    }

    @Test(timeout = 4000)
    public void testTranslateStringWithoutEntities() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);

        unescaper.translate("D+RTgb,eb:&],ms", writer);

        assertEquals("D+RTgb,eb:&],ms", writer.toString());
    }

    @Test(timeout = 4000)
    public void testTranslateCharBufferWithIncompleteEntity() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'\u0000', '&'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        unescaper.translate(charBuffer, writer);

        assertEquals(0, charBuffer.position());
    }

    @Test(timeout = 4000)
    public void testTranslateSingleCharacter() throws Throwable {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);

        int result = unescaper.translate("0", 0, null);

        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testTranslateValidNumericEntity() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'\u0000', '\u0000', '\u0000', '&', '#', '3', ';'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        int result = unescaper.translate(charBuffer, 3, writer);

        assertEquals("\u0003", writer.toString());
        assertEquals(4, result);
    }

    @Test(timeout = 4000)
    public void testTranslateWithStringIndexOutOfBoundsException() throws Throwable {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        StringWriter writer = new StringWriter(1416);
        StringBuffer buffer = writer.getBuffer();

        try {
            unescaper.translate(buffer, 1416, writer);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testTranslateWithNullPointerException() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);

        try {
            unescaper.translate(null, Integer.MAX_VALUE, writer);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.apache.commons.lang3.text.translate.NumericEntityUnescaper", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateWithIndexOutOfBoundsException() throws Throwable {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        CharBuffer charBuffer = CharBuffer.allocate(2261);
        StringWriter writer = new StringWriter();

        try {
            unescaper.translate(charBuffer, 2261, writer);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testTranslateHexadecimalEntity() throws Throwable {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'\u0000', '\u0000', '&', '#', 'x', ';'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        String result = unescaper.translate(charBuffer);

        assertEquals("\u0000\u0000&#x;", result);
    }

    @Test(timeout = 4000)
    public void testTranslateIncompleteHexadecimalEntity() throws Throwable {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'\u0000', '\u0000', '\u0000', '\u0000', '&', '#', 'x'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        String result = unescaper.translate(charBuffer);

        assertEquals("\u0000\u0000\u0000\u0000&#x\u0000", result);
    }

    @Test(timeout = 4000)
    public void testTranslateUpperCaseHexadecimalEntity() throws Throwable {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '&', '#', 'X'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        String result = unescaper.translate(charBuffer);

        assertEquals("\u0000\u0000\u0000\u0000\u0000&#X", result);
    }

    @Test(timeout = 4000)
    public void testTranslateCharBufferWithArray() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);
        char[] charArray = new char[]{'&'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        unescaper.translate(charBuffer, writer);

        assertTrue(charBuffer.hasArray());
    }

    @Test(timeout = 4000)
    public void testTranslateStringWithoutEntities2() throws Throwable {
        StringWriter writer = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(EMPTY_OPTIONS);

        unescaper.translate("D+RTgb,eb:&s", writer);

        assertEquals("D+RTgb,eb:&s", writer.toString());
    }

    @Test(timeout = 4000)
    public void testNullOptionArrayThrowsException() throws Throwable {
        NumericEntityUnescaper.OPTION[] options = new NumericEntityUnescaper.OPTION[1];

        try {
            new NumericEntityUnescaper(options);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("java.util.EnumSet", e);
        }
    }
}