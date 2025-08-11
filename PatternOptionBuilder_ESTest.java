package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class PatternOptionBuilderTest {

    // Helper to assert the mapping from a pattern code to its value type.
    private static void assertValueType(char code, Class<?> expected) {
        assertSame("Unexpected value type for code '" + code + "'", expected, PatternOptionBuilder.getValueType(code));
    }

    // Helper to assert that a code is not a value code and maps to no type.
    private static void assertUnknownCode(char code) {
        assertNull("Expected no type for unknown code '" + code + "'", PatternOptionBuilder.getValueType(code));
        assertFalse("Expected isValueCode to be false for '" + code + "'", PatternOptionBuilder.isValueCode(code));
    }

    // getValueType: known mappings
    @Test
    public void getValueType_returnsExpectedTypes_forKnownCodes() {
        assertValueType(':', String.class);
        assertValueType('%', Number.class);
        assertValueType('#', Date.class);
        assertValueType('+', Class.class);
        assertValueType('@', Object.class);
        assertValueType('<', FileInputStream.class);
        assertValueType('>', File.class);
        assertValueType('*', File[].class);
        assertValueType('/', URL.class);
    }

    // getValueType: unknown mappings
    @Test
    public void getValueType_returnsNull_forUnknownCodes() {
        assertUnknownCode(',');
        assertUnknownCode('.');
        assertUnknownCode('?');
        assertUnknownCode('$');
        assertUnknownCode('=');
        assertUnknownCode('&');
        assertUnknownCode('1');
        assertUnknownCode('9');
        assertUnknownCode('a');
        assertUnknownCode('Z');
    }

    // getValueClass (deprecated) delegates to the same mapping
    @Test
    public void getValueClass_deprecated_stillReturnsMappedClass() {
        Object value = PatternOptionBuilder.getValueClass(':');
        assertTrue(value instanceof Class<?>);
        assertSame(String.class, value);

        assertNull(PatternOptionBuilder.getValueClass('x'));
    }

    // isValueCode: must be true for codes that map to a value type
    @Test
    public void isValueCode_trueForAllCodesThatHaveAValueType() {
        for (char c : new char[] { ':', '%', '#', '+', '@', '<', '>', '*', '/' }) {
            assertTrue("Expected isValueCode to be true for '" + c + "'", PatternOptionBuilder.isValueCode(c));
        }
    }

    // isValueCode: must be false for non-value characters
    @Test
    public void isValueCode_falseForNonValueCodes() {
        for (char c : new char[] { ',', '.', '?', '$', '=', '&', '1', 'a' }) {
            assertFalse("Expected isValueCode to be false for '" + c + "'", PatternOptionBuilder.isValueCode(c));
        }
    }

    // parsePattern: happy path from the class Javadoc ("vp:!f/")
    @Test
    public void parsePattern_buildsOptionsAccordingToPattern_specExample() {
        Options options = PatternOptionBuilder.parsePattern("vp:!f/");

        // -v is a flag (no arg)
        Option v = options.getOption("v");
        assertNotNull(v);
        assertFalse(v.hasArg());
        assertFalse(v.isRequired());

        // -p expects a String
        Option p = options.getOption("p");
        assertNotNull(p);
        assertTrue(p.hasArg());
        assertEquals(PatternOptionBuilder.STRING_VALUE, p.getType());
        assertFalse(p.isRequired());

        // -f is required and expects a URL
        Option f = options.getOption("f");
        assertNotNull(f);
        assertTrue(f.hasArg());
        assertTrue(f.isRequired());
        assertEquals(PatternOptionBuilder.URL_VALUE, f.getType());
    }

    // parsePattern: empty pattern yields empty Options
    @Test
    public void parsePattern_emptyPatternYieldsNoOptions() {
        Options options = PatternOptionBuilder.parsePattern("");
        assertNotNull(options);
        assertTrue(options.getOptions().isEmpty());
    }

    // parsePattern: null pattern throws NPE
    @Test
    public void parsePattern_nullThrowsNullPointerException() {
        try {
            PatternOptionBuilder.parsePattern(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    // parsePattern: illegal option name '=' triggers IAE
    @Test
    public void parsePattern_illegalOptionNameEquals_throwsIllegalArgumentException() {
        try {
            PatternOptionBuilder.parsePattern("=");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // message comes from OptionValidator, keep assertion loose
            assertTrue(expected.getMessage() == null || expected.getMessage().contains("Illegal option name"));
        }
    }

    // parsePattern: illegal character, e.g., a single quote, triggers IAE
    @Test
    public void parsePattern_illegalCharacterQuote_throwsIllegalArgumentException() {
        try {
            PatternOptionBuilder.parsePattern("'");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }
}