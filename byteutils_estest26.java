package org.apache.commons.compress.utils;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Verifies that calling {@link ByteUtils#fromLittleEndian(ByteUtils.ByteSupplier, int)}
     * with a null supplier throws a {@link NullPointerException}.
     */
    @Test
    public void fromLittleEndianWithNullSupplierShouldThrowNullPointerException() {
        // Arrange: Define the arguments for the method under test.
        // The supplier is null, which is the condition we are testing.
        final ByteUtils.ByteSupplier nullSupplier = null;
        // The length value is arbitrary as the method should fail before using it.
        final int length = 1;

        // Act & Assert: Verify that executing the method throws the expected exception.
        assertThrows(NullPointerException.class, () -> {
            ByteUtils.fromLittleEndian(nullSupplier, length);
        });
    }
}