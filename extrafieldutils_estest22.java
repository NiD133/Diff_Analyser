package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that {@link ExtraFieldUtils#fillExtraField(ZipExtraField, byte[], int, int, boolean)}
     * throws an {@link IllegalArgumentException} when provided with a negative length that
     * results in an invalid range for an underlying data copy operation.
     *
     * <p>The method under test delegates to a method in the {@code ZipExtraField} instance,
     * which in this case calls {@code Arrays.copyOfRange(data, offset, offset + length)}.
     * When the 'length' argument is negative, the start offset can be greater than the
     * calculated end offset, which is an invalid range that causes the exception.</p>
     */
    @Test
    public void fillExtraFieldThrowsIllegalArgumentExceptionForNegativeLength() {
        // Arrange: Set up the test inputs.
        final ZipExtraField extraField = new X0019_EncryptionRecipientCertificateList();
        final byte[] data = new byte[0];
        final int offset = -1100;
        final int length = -1100; // A negative length causes an invalid range.
        final boolean isLocalData = false;

        // Act & Assert: Execute the method and verify the outcome.
        try {
            ExtraFieldUtils.fillExtraField(extraField, data, offset, length, isLocalData);
            fail("Expected an IllegalArgumentException because the calculated range is invalid.");
        } catch (final IllegalArgumentException e) {
            // This is the expected exception.
            // We verify the message to ensure it's thrown for the correct reason.
            // The message format "from > to" is specified by java.util.Arrays.
            final String expectedMessage = offset + " > " + (offset + length);
            assertEquals("The exception message should detail the invalid range.",
                    expectedMessage, e.getMessage());
        } catch (final Exception e) {
            // Fail the test if any other unexpected exception is thrown.
            fail("Expected an IllegalArgumentException, but caught " + e.getClass().getName());
        }
    }
}