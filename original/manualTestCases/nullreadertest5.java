package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testReadCharArray() throws Exception {
        final char[] chars = new char[10];
        final Reader reader = new TestNullReader(15);
        // Read into array
        final int count1 = reader.read(chars);
        assertEquals(chars.length, count1, "Read 1");
        for (int i = 0; i < count1; i++) {
            assertEquals(i, chars[i], "Check Chars 1");
        }
        // Read into array
        final int count2 = reader.read(chars);
        assertEquals(5, count2, "Read 2");
        for (int i = 0; i < count2; i++) {
            assertEquals(count1 + i, chars[i], "Check Chars 2");
        }
        // End of File
        final int count3 = reader.read(chars);
        assertEquals(-1, count3, "Read 3 (EOF)");
        // Test reading after the end of file
        try {
            final int count4 = reader.read(chars);
            fail("Should have thrown an IOException, value=[" + count4 + "]");
        } catch (final IOException e) {
            assertEquals("Read after end of file", e.getMessage());
        }
        // reset by closing
        reader.close();
        // Read into array using offset & length
        final int offset = 2;
        final int lth = 4;
        final int count5 = reader.read(chars, offset, lth);
        assertEquals(lth, count5, "Read 5");
        for (int i = offset; i < lth; i++) {
            assertEquals(i, chars[i], "Check Chars 3");
        }
    }
}
