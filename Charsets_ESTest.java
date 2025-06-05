package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.SortedMap;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class CharsetsTest extends Charsets_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testToCharsetWithNullAndDefaultUTF8() {
        // Test that toCharset returns UTF-8 when given null and UTF-8 as default
        Charset defaultCharset = Charsets.UTF_8;
        Charset resultCharset = Charsets.toCharset(null, defaultCharset);
        assertNotNull(resultCharset);
        assertEquals("UTF-8", resultCharset.toString());
    }

    @Test(timeout = 4000)
    public void testToCharsetWithNullDefaultsToUTF8() {
        // Test that toCharset returns UTF-8 when given null
        Charset resultCharset = Charsets.toCharset(null);
        assertEquals("UTF-8", resultCharset.toString());
        assertNotNull(resultCharset);
    }

    @Test(timeout = 4000)
    public void testToCharsetWithUSASCII() {
        // Test that toCharset returns US-ASCII when given US-ASCII
        Charset usAsciiCharset = Charsets.US_ASCII;
        Charset resultCharset = Charsets.toCharset(usAsciiCharset, usAsciiCharset);
        assertEquals("US-ASCII", resultCharset.toString());
    }

    @Test(timeout = 4000)
    public void testToCharsetWithUSASCIIFromInstance() {
        // Test that toCharset returns US-ASCII when using an instance of Charsets
        Charsets charsetsInstance = new Charsets();
        Charset resultCharset = Charsets.toCharset(charsetsInstance.US_ASCII);
        assertEquals("US-ASCII", resultCharset.displayName());
        assertEquals("ISO-8859-1", resultCharset.name());
    }

    @Test(timeout = 4000)
    public void testToCharsetWithNullStringAndNullDefault() {
        // Test that toCharset returns null when both string and default are null
        Charset resultCharset = Charsets.toCharset((String) null, (Charset) null);
        assertNull(resultCharset);
    }

    @Test(timeout = 4000)
    public void testToCharsetWithNullStringAndUSASCII() {
        // Test that toCharset returns US-ASCII when given null string and US-ASCII as default
        Charsets charsetsInstance = new Charsets();
        Charset resultCharset = Charsets.toCharset((String) null, charsetsInstance.US_ASCII);
        assertEquals("US-ASCII", resultCharset.toString());
    }

    @Test(timeout = 4000)
    public void testToCharsetWithISO8859_2() {
        // Test that toCharset returns ISO-8859-2 when given "L2"
        Charset resultCharset = Charsets.toCharset("L2");
        assertEquals("ISO-8859-2", resultCharset.toString());
    }

    @Test(timeout = 4000)
    public void testToCharsetWithIllegalCharsetName() {
        // Test that toCharset throws IllegalCharsetNameException for invalid charset name
        Charset defaultCharset = Charsets.UTF_8;
        try {
            Charsets.toCharset("Tv&)_", defaultCharset);
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testToCharsetWithIllegalCharsetNameOnly() {
        // Test that toCharset throws IllegalCharsetNameException for invalid charset name
        try {
            Charsets.toCharset("?");
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testToCharsetWithUnsupportedCharset() {
        // Test that toCharset throws UnsupportedCharsetException for unsupported charset
        Charsets charsetsInstance = new Charsets();
        try {
            Charsets.toCharset("org.apache.commons.io.Charsets", charsetsInstance.UTF_8);
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testToCharsetWithNullCharset() {
        // Test that toCharset returns null when both charsets are null
        Charset resultCharset = Charsets.toCharset((Charset) null, (Charset) null);
        assertNull(resultCharset);
    }

    @Test(timeout = 4000)
    public void testToCharsetWithUnsupportedCharsetName() {
        // Test that toCharset throws UnsupportedCharsetException for unsupported charset name
        try {
            Charsets.toCharset("org.apache.commons.io.serialization.ObjectStreamClassPredicate");
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testRequiredCharsetsNotEmpty() {
        // Test that requiredCharsets returns a non-empty map
        SortedMap<String, Charset> charsetMap = Charsets.requiredCharsets();
        assertFalse(charsetMap.isEmpty());
    }
}