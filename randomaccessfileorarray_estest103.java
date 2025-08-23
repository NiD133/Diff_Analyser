package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

// Note: Unused imports from the original test have been removed for clarity.

public class RandomAccessFileOrArray_ESTestTest103 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the constructor {@link RandomAccessFileOrArray#RandomAccessFileOrArray(String, boolean, boolean)}
     * throws a {@link NullPointerException} when the filename argument is null.
     */
    @Test
    @SuppressWarnings("deprecation") // Testing a deprecated constructor is the explicit goal of this test.
    public void constructorWithFilenameThrowsNPEIfFilenameIsNull() {
        // The constructor is expected to throw a NullPointerException because it internally
        // attempts to create a `java.io.File` with a null path, which is not allowed.
        NullPointerException thrown = assertThrows(
            NullPointerException.class,
            () -> new RandomAccessFileOrArray((String) null, true, true)
        );

        // The NPE thrown by `new java.io.File(null)` has no message.
        // This assertion confirms we're catching the expected exception from the expected source.
        assertNull("The NullPointerException should have no message.", thrown.getMessage());
    }
}