package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;
import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testToCharset_String() {
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8));
    }
}
