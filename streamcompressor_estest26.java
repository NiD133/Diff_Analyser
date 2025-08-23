package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Test suite for {@link StreamCompressor}.
 * This class contains the improved version of the original test.
 */
public class StreamCompressor_ESTestTest26 { // Retaining original class name for context

    /**
     * Verifies that calling deflate() on a StreamCompressor created with a null
     * ScatterGatherBackingStore throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void deflateWithNullBackingStoreThrowsNullPointerException() throws IOException {
        // Arrange: Create a StreamCompressor with a null backing store.
        // The factory method is expected to create an instance, but any subsequent
        // operation on it should fail.
        StreamCompressor compressor = StreamCompressor.create(
            ZipEntry.DEFLATED, (ScatterGatherBackingStore) null);

        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);

        // Act: Attempt to deflate an empty stream. This should trigger the use of the
        // null backing store, causing a NullPointerException.
        compressor.deflate(emptyInput, ZipEntry.DEFLATED);

        // Assert: The @Test(expected = NullPointerException.class) annotation handles the
        // verification that the expected exception was thrown.
    }
}