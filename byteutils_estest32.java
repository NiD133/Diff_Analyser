package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.DataInput;
import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(DataInput, int)} throws an
     * IllegalArgumentException when the requested read length is greater than 8.
     * A long value can hold at most 8 bytes.
     */
    @Test
    public void fromLittleEndianWithDataInputThrowsIfLengthIsTooLarge() {
        // Arrange: Define a length that is too large to be read into a long.
        final int invalidLength = 9; // Max allowed is 8.
        final DataInput nullDataInput = null; // The input is not used before the length check.
        final String expectedErrorMessage = "Can't read more than eight bytes into a long value";

        // Act & Assert: Call the method and verify that it throws the correct exception.
        try {
            ByteUtils.fromLittleEndian(nullDataInput, invalidLength);
            fail("Expected an IllegalArgumentException because the length is greater than 8.");
        } catch (final IllegalArgumentException e) {
            // This is the expected outcome.
            assertEquals(expectedErrorMessage, e.getMessage());
        } catch (final Exception e) {
            // Fail the test if any other unexpected exception is thrown.
            fail("Caught an unexpected exception: " + e);
        }
    }
}