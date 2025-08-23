package com.google.common.hash;

import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import org.junit.Test;

// The test class name and inheritance are preserved from the original.
public class AbstractStreamingHasher_ESTestTest4 extends AbstractStreamingHasher_ESTest_scaffolding {

    /**
     * Verifies that a hasher instance cannot be updated after its {@code hash()} method
     * has been called. Attempting to add more data to a finalized hasher should
     * result in an {@code IllegalStateException}.
     */
    @Test
    public void updateAfterHashingThrowsIllegalStateException() {
        // Arrange: Create a hasher and provide it with some initial data.
        // We use Crc32cHasher as a concrete implementation of the abstract class under test.
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.putBytes(ByteBuffer.allocate(16));

        // Act: Compute the hash. This action finalizes the hasher's state,
        // rendering it unusable for further updates.
        hasher.hash();

        // Assert: Verify that any subsequent attempt to add data throws an exception.
        try {
            hasher.putShort((short) 123);
            fail("Expected an IllegalStateException because the hasher was already used.");
        } catch (IllegalStateException expected) {
            // This is the correct behavior. A hasher instance should not be reusable
            // after the final hash has been computed.
        }
    }
}