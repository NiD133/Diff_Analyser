package org.apache.commons.io.output;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for {@link ThresholdingOutputStream} and its subclasses.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that write(byte[], int, int) throws an IndexOutOfBoundsException for invalid
     * offset and length arguments.
     *
     * <p>The original test was auto-generated and used a null buffer along with a negative
     * offset and length. A null buffer would cause a NullPointerException, masking the
     * intended IndexOutOfBoundsException. This improved test uses a valid, non-null buffer to
     * correctly test the bounds-checking logic.</p>
     */
    @Test
    public void testWriteWithNegativeOffsetAndLengthThrowsIndexOutOfBoundsException() throws IOException {
        // Arrange: A DeferredFileOutputStream is used as a concrete implementation of
        // ThresholdingOutputStream. It initially buffers data in memory.
        try (final DeferredFileOutputStream stream = new DeferredFileOutputStream.Builder().get()) {
            final byte[] dummyBuffer = new byte[0];

            // Act & Assert: The write method should reject negative offset and length values.
            assertThrows(IndexOutOfBoundsException.class, () -> {
                stream.write(dummyBuffer, -122, -122);
            });
        }
    }
}