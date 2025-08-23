package org.apache.commons.codec.digest;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that calling hash64 with a null String input throws a NullPointerException.
     */
    @Test
    public void hash64ShouldThrowNullPointerExceptionForNullStringInput() {
        // The hash64(String) method is expected to throw a NullPointerException
        // when the input is null, which is standard behavior for such utility methods.
        assertThrows(NullPointerException.class, () -> {
            // The cast to (String) is necessary to resolve ambiguity between
            // hash64(String) and hash64(byte[], int).
            MurmurHash2.hash64((String) null);
        });
    }
}