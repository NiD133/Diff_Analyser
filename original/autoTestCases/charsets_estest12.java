package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetsTest {

    @Test(expected = UnsupportedCharsetException.class)
    public void testToCharset_UnsupportedCharsetName() {
        // Test case to verify that Charsets.toCharset() throws UnsupportedCharsetException
        // when given an invalid or unsupported charset name.
        try {
            Charsets.toCharset("invalid-charset-name"); // Use a more descriptive, obviously invalid name
        } catch (UnsupportedCharsetException e) {
            // Expected exception, re-throw to satisfy the @Test(expected=...) annotation
            throw e;
        }
    }

    // Optional: Add a test case for a valid charset to showcase proper usage

    @Test
    public void testToCharset_ValidCharsetName() {
        // Test case to verify that Charsets.toCharset() correctly returns a Charset object
        // when given a valid charset name.
        Charset utf8Charset = Charsets.toCharset("UTF-8");
        assertNotNull(utf8Charset);
        assertEquals("UTF-8", utf8Charset.name());
    }

}