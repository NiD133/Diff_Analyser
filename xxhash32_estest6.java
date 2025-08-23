package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Contains tests for the {@link XXHash32} class.
 */
public class XXHash32Test {

    /**
     * Tests that the hash is calculated correctly after multiple, non-sequential
     * update calls using different segments of the same byte array. This verifies
     * the stateful accumulation of the hash.
     */
    @Test
    public void testGetValueAfterMultipleUpdatesOnByteArraySegments() {
        // Arrange
        // The expected hash is a "golden value", pre-calculated to be the correct
        // result for the specific sequence of updates performed in this test.
        final long expectedHash = 1465785993L;
        final XXHash32 hasher = new XXHash32();

        // Create an input byte array. Most bytes are 0, with one non-zero value
        // to ensure the data being hashed is not trivial.
        final byte[] inputData = new byte[25];
        inputData[1] = 16;

        // Act
        // Update the hash with three different segments from the input data.
        // The order of these updates is critical to the final hash value.
        // The sequence of data processed is:
        // 1. Bytes from inputData[16] to inputData[19]
        hasher.update(inputData, 16, 4);
        // 2. Bytes from inputData[1] to inputData[4] (includes the non-zero byte)
        hasher.update(inputData, 1, 4);
        // 3. Bytes from inputData[0] to inputData[15]
        hasher.update(inputData, 0, 16);

        final long actualHash = hasher.getValue();

        // Assert
        assertEquals("The calculated hash should match the pre-computed value.", expectedHash, actualHash);
    }
}