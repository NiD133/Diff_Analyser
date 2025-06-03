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
    public void testRequiredCharsets() {
        final SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();
        // test for what we expect to be there as of Java 6
        // Make sure the object at the given key is the right one
        assertEquals(requiredCharsets.get("US-ASCII").name(), "US-ASCII");
        assertEquals(requiredCharsets.get("ISO-8859-1").name(), "ISO-8859-1");
        assertEquals(requiredCharsets.get("UTF-8").name(), "UTF-8");
        assertEquals(requiredCharsets.get("UTF-16").name(), "UTF-16");
        assertEquals(requiredCharsets.get("UTF-16BE").name(), "UTF-16BE");
        assertEquals(requiredCharsets.get("UTF-16LE").name(), "UTF-16LE");
    }
}
