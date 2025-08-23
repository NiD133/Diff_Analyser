package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the GzipCompressorOutputStream class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the
     * GzipParameters argument is null, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenParametersAreNull() throws IOException {
        // Attempt to create an instance with null parameters, which should fail.
        new GzipCompressorOutputStream(new ByteArrayOutputStream(), null);
    }
}