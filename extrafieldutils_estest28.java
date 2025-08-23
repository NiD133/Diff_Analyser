package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that fillExtraField throws a NullPointerException when the data buffer argument is null.
     * This verifies that the method does not handle null input gracefully and fails fast,
     * which is the expected behavior for a low-level utility method.
     */
    @Test(expected = NullPointerException.class)
    public void fillExtraFieldWithNullBufferThrowsNullPointerException() throws ZipException {
        // Arrange: Create an extra field instance and define parameters for the method call.
        // The byte array is intentionally set to null to trigger the exception.
        final ResourceAlignmentExtraField extraField = new ResourceAlignmentExtraField();
        final byte[] nullDataBuffer = null;
        final int offset = 2;
        final int length = 2;
        final boolean isLocal = true;

        // Act: Call the method under test with the null buffer.
        ExtraFieldUtils.fillExtraField(extraField, nullDataBuffer, offset, length, isLocal);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // as specified by the `expected` attribute in the @Test annotation.
    }
}