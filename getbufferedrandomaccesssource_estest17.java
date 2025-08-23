package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link GetBufferedRandomAccessSource}.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get() method propagates an IllegalArgumentException from the
     * underlying source when the requested position is invalid.
     * <p>
     * This test specifically checks the case where the underlying source is a
     * {@link ByteBufferRandomAccessSource}, which throws an exception if the
     * requested position exceeds Integer.MAX_VALUE.
     */
    @Test
    public void get_withPositionExceedingIntegerMaxValue_throwsIllegalArgumentException() throws IOException {
        // Arrange: Create a buffered source wrapping a source that validates position.
        byte[] sourceBytes = new byte[10];
        ByteBufferRandomAccessSource underlyingSource = new ByteBufferRandomAccessSource(ByteBuffer.wrap(sourceBytes));
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(underlyingSource);

        // An invalid position that is greater than the maximum integer value.
        long invalidPosition = (long) Integer.MAX_VALUE + 1;

        // Act & Assert
        try {
            bufferedSource.get(invalidPosition);
            fail("Should have thrown an IllegalArgumentException for a position exceeding Integer.MAX_VALUE.");
        } catch (IllegalArgumentException e) {
            // The exception is expected. Verify its message is propagated correctly.
            assertEquals("Position must be less than Integer.MAX_VALUE", e.getMessage());
        }
    }
}