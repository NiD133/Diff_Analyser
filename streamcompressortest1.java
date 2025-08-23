package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StreamCompressor} factory methods.
 */
class StreamCompressorTest {

    @Test
    void createWithDataOutputShouldReturnNonNullInstance() throws IOException {
        // Arrange: Set up the necessary components for the test.
        // A DataOutput is required by the factory method. We use a DataOutputStream
        // wrapping a ByteArrayOutputStream as a test-friendly implementation.
        final DataOutput dataOutput = new DataOutputStream(new ByteArrayOutputStream());
        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);

        // Act: Call the method under test.
        // The try-with-resources statement ensures the compressor is closed automatically.
        try (StreamCompressor compressor = StreamCompressor.create(dataOutput, deflater)) {
            // Assert: Verify the outcome.
            assertNotNull(compressor, "The factory method should successfully create a non-null StreamCompressor instance.");
        }
    }
}