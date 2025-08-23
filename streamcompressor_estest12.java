package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that calling writeCounted(byte[]) with a null array
     * throws a NullPointerException, as this is an invalid argument.
     */
    @Test
    public void writeCountedWithNullArrayShouldThrowNullPointerException() {
        // Arrange: Create a StreamCompressor instance. The specific type or
        // compression level is not relevant for this test, as the null check
        // should occur before any compression logic.
        final int anyValidCompressionLevel = 8;
        StreamCompressor streamCompressor = StreamCompressor.create(anyValidCompressionLevel, (ScatterGatherBackingStore) null);

        // Act & Assert: Expect a NullPointerException when the method is called with null.
        assertThrows(NullPointerException.class, () -> streamCompressor.writeCounted((byte[]) null));
    }
}