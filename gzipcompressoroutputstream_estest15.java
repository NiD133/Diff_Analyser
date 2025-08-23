package org.apache.commons.compress.compressors.gzip;

import java.io.IOException;
import java.io.OutputStream;
import org.junit.Test;

/**
 * Unit tests for the {@link GzipCompressorOutputStream} class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that the constructor throws a NullPointerException
     * when initialized with a null OutputStream, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullOutputStream() throws IOException {
        // Attempting to create an instance with a null stream should fail immediately.
        new GzipCompressorOutputStream((OutputStream) null);
    }
}