package org.apache.commons.io;

import org.junit.Test;

/**
 * Tests for the {@link ByteOrderMark} class, focusing on constructor argument validation.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the ByteOrderMark constructor throws a NullPointerException
     * when the 'bytes' array argument is null. The constructor's contract
     * requires a non-null array of bytes.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullBytes() {
        // Attempt to create a ByteOrderMark with a null bytes array,
        // which is expected to fail with a NullPointerException.
        new ByteOrderMark("UTF-8", (int[]) null);
    }
}