package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for exceptional cases in the {@link ByteOrderMark} constructor.
 */
class ByteOrderMarkConstructorExceptionTest {

    @Test
    void constructorShouldThrowNullPointerExceptionWhenCharsetNameIsNull() {
        // The constructor should reject a null charset name.
        final NullPointerException e = assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));
        assertEquals("charsetName", e.getMessage());
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionWhenCharsetNameIsEmpty() {
        // The constructor should reject an empty charset name.
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));
        assertEquals("No charsetName specified", e.getMessage());
    }

    @Test
    void constructorShouldThrowNullPointerExceptionWhenBytesAreNull() {
        // The constructor should reject a null byte array.
        final NullPointerException e = assertThrows(NullPointerException.class, () -> new ByteOrderMark("test-charset", (int[]) null));
        assertEquals("bytes", e.getMessage());
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionWhenBytesAreEmpty() {
        // The constructor uses a varargs parameter for bytes, so calling it with no
        // byte arguments results in an empty array, which should be rejected.
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("test-charset"));
        assertEquals("No bytes specified", e.getMessage());
    }
}