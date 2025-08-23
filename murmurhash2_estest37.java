package org.apache.commons.codec.digest;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on edge cases and invalid inputs.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash32() throws a NullPointerException when the input data array is null.
     * This is the expected behavior as the method cannot compute a hash from a null array.
     */
    @Test
    public void hash32ShouldThrowNullPointerExceptionWhenDataIsNull() {
        // Define arbitrary values for length and seed. Their specific values are
        // irrelevant here, as the NullPointerException should be thrown before
        // they are used.
        final int anyLength = 10;
        final int anySeed = 12345;

        // Verify that calling hash32 with a null data array throws NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            MurmurHash2.hash32(null, anyLength, anySeed);
        });
    }
}