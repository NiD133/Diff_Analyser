package org.apache.commons.io;

import org.junit.Test;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;

public class CharsetsTest {

    @Test
    public void testToCharset_US_ASCII() {
        // Verify that converting the US_ASCII character set name to a Charset object works correctly.
        // Charsets.US_ASCII refers to the standard US-ASCII character set.

        Charset asciiCharset = Charsets.toCharset("US-ASCII");

        // Assert that the returned Charset's name is "US-ASCII".  This confirms that the correct character set was retrieved.
        assertEquals("US-ASCII", asciiCharset.name());
    }
}