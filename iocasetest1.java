package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class IOCaseTestTest1 {

    private static final boolean WINDOWS = File.separatorChar == '\\';

    private void assert0(final byte[] arr) {
        for (final byte e : arr) {
            assertEquals(0, e);
        }
    }

    private void assert0(final char[] arr) {
        for (final char e : arr) {
            assertEquals(0, e);
        }
    }

    private IOCase serialize(final IOCase value) throws Exception {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(buf)) {
            out.writeObject(value);
            out.flush();
        }
        final ByteArrayInputStream bufin = new ByteArrayInputStream(buf.toByteArray());
        final ObjectInputStream in = new ObjectInputStream(bufin);
        return (IOCase) in.readObject();
    }

    @Test
    void test_checkCompare_case() {
        assertEquals(0, IOCase.SENSITIVE.checkCompareTo("ABC", "ABC"));
        assertTrue(IOCase.SENSITIVE.checkCompareTo("ABC", "abc") < 0);
        assertTrue(IOCase.SENSITIVE.checkCompareTo("abc", "ABC") > 0);
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "ABC"));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("ABC", "abc"));
        assertEquals(0, IOCase.INSENSITIVE.checkCompareTo("abc", "ABC"));
        assertEquals(0, IOCase.SYSTEM.checkCompareTo("ABC", "ABC"));
        assertEquals(WINDOWS, IOCase.SYSTEM.checkCompareTo("ABC", "abc") == 0);
        assertEquals(WINDOWS, IOCase.SYSTEM.checkCompareTo("abc", "ABC") == 0);
    }
}
