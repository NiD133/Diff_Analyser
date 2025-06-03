package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Charsets} utility class.  Focuses on the {@code toCharset} methods.
 */
public class CharsetsTest {

    @Test
    public void testToCharset_String_Charset() {
        // Test case 1: Null charset name, null default charset
        // Expected: null should be returned.
        assertNull(Charsets.toCharset((String) null, null));

        // Test case 2: Null charset name, default charset specified
        // Expected: The default charset should be returned.
        Charset defaultCharset = Charset.defaultCharset();
        assertEquals(defaultCharset, Charsets.toCharset((String) null, defaultCharset));
    }

    @Test
    public void testToCharset_Charset_Charset() {
         // Test case 3: Null charset, null default charset
        // Expected: null should be returned.
        assertNull(Charsets.toCharset((Charset) null, null));

        // Test case 4: Null charset, default charset specified
        // Expected: The default charset should be returned.
        Charset defaultCharset = Charset.defaultCharset();
        assertEquals(defaultCharset, Charsets.toCharset((Charset) null, defaultCharset));

        // Test case 5: Default charset, default charset specified
        // Expected: The default charset should be returned (identity).
        Charset defaultCharset2 = Charset.defaultCharset();
        assertEquals(defaultCharset2, Charsets.toCharset(defaultCharset2, defaultCharset2));

        // Test case 6: UTF-8 charset, default charset specified
        // Expected: UTF-8 should be returned (it's preferred).
        Charset defaultCharset3 = Charset.defaultCharset();
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, defaultCharset3));

        // Test case 7: UTF-8 charset, null default charset
        // Expected: UTF-8 should be returned (it's preferred and non-null).
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, null));
    }
}