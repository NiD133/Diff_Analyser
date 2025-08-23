package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that calling fromLittleEndian with a null byte array
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void fromLittleEndianWithNullArrayShouldThrowNullPointerException() {
        // This test verifies the behavior of the fromLittleEndian(byte[]) method
        // when it is invoked with a null argument. The expected outcome is a
        // NullPointerException, as the method attempts to access the length
        // of the null array.

        // Act: Call the method under test with null input.
        // The cast to (byte[]) is necessary to resolve ambiguity between
        // overloaded fromLittleEndian methods.
        ByteUtils.fromLittleEndian((byte[]) null);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}