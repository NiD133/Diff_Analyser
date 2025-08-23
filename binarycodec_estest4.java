package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the static utility methods in the {@link BinaryCodec} class.
 *
 * Note: This test focuses on the org.apache.commons.codec.binary.BinaryCodec class,
 * not the org.locationtech.spatial4j.io.BinaryCodec class provided in the prompt's
 * source code section, as the original test targets the former.
 */
public class BinaryCodecTest {

    /**
     * Tests that {@link BinaryCodec#isEmpty(byte[])} returns false for a non-empty byte array.
     */
    @Test
    public void isEmpty_shouldReturnFalse_forNonEmptyArray() {
        // Arrange: Create a byte array that is not null and has a length greater than zero.
        byte[] nonEmptyArray = new byte[8];

        // Act: Call the method under test.
        boolean result = BinaryCodec.isEmpty(nonEmptyArray);

        // Assert: Verify that the method returns false.
        assertFalse("isEmpty() should return false for a non-empty array", result);
    }

    /**
     * Tests that {@link BinaryCodec#isEmpty(byte[])} returns true for an empty byte array.
     */
    @Test
    public void isEmpty_shouldReturnTrue_forEmptyArray() {
        // Arrange
        byte[] emptyArray = new byte[0];

        // Act
        boolean result = BinaryCodec.isEmpty(emptyArray);

        // Assert
        assertTrue("isEmpty() should return true for an empty (zero-length) array", result);
    }

    /**
     * Tests that {@link BinaryCodec#isEmpty(byte[])} returns true for a null byte array.
     */
    @Test
    public void isEmpty_shouldReturnTrue_forNullArray() {
        // Arrange
        byte[] nullArray = null;

        // Act
        boolean result = BinaryCodec.isEmpty(nullArray);

        // Assert
        assertTrue("isEmpty() should return true for a null array", result);
    }
}