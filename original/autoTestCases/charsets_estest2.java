package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

/**
 * Test cases for the {@link Charsets} utility class.  Focuses on testing the
 * `toCharset` methods to ensure they handle null and default Charset behavior
 * correctly.
 */
public class CharsetsTest {

    /**
     * Tests the `toCharset(Charset)` method when given a null Charset.
     * It verifies that the method returns the default Charset (UTF-8) in this case.
     */
    @Test
    public void testToCharset_NullCharset() {
        // Arrange: Pass in a null Charset.
        Charset inputCharset = null;

        // Act: Call the toCharset method with the null Charset.
        Charset resultCharset = Charsets.toCharset(inputCharset);

        // Assert: Verify that the returned Charset is the default (UTF-8).
        assertNotNull("The resulting Charset should not be null.", resultCharset);
        assertEquals("The resulting Charset should be UTF-8.", "UTF-8", resultCharset.toString());
    }

    /**
     * Tests the `toCharset(Charset, Charset)` method when given a null Charset and a null default Charset.
     * It verifies that the method returns the default Charset (UTF-8) in this case.
     */
    @Test
    public void testToCharset_NullCharsetAndDefault() {
        // Arrange: Pass in a null Charset and a null default Charset.
        Charset inputCharset = null;
        Charset defaultCharset = null;

        // Act: Call the toCharset method with the null Charset and default.
        Charset resultCharset = Charsets.toCharset(inputCharset, defaultCharset);

        // Assert: Verify that the returned Charset is the default (UTF-8).
        assertNotNull("The resulting Charset should not be null.", resultCharset);
        assertEquals("The resulting Charset should be UTF-8.", "UTF-8", resultCharset.toString());
    }

}