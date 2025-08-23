package com.itextpdf.text.pdf;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the static utility methods in {@link MappedRandomAccessFile}.
 */
public class MappedRandomAccessFileTest {

    /**
     * Verifies that the clean() method successfully processes a direct ByteBuffer
     * with zero capacity and returns true, indicating a successful operation.
     */
    @Test
    public void clean_shouldReturnTrue_whenCleaningZeroCapacityDirectBuffer() {
        // Arrange: Create a direct ByteBuffer with zero capacity. This is a valid edge case.
        ByteBuffer zeroCapacityBuffer = ByteBuffer.allocateDirect(0);

        // Act: Attempt to clean the buffer using the method under test.
        boolean wasCleanSuccessful = MappedRandomAccessFile.clean(zeroCapacityBuffer);

        // Assert: The clean operation should report success.
        assertTrue("The clean operation on a zero-capacity direct buffer should succeed.", wasCleanSuccessful);
    }
}