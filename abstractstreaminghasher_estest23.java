package com.google.common.hash;

import static org.junit.Assert.assertSame;

import com.google.common.hash.Crc32cHashFunction.Crc32cHasher;
import org.junit.Test;

/**
 * Tests for the fluent API behavior of {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    @Test
    public void putBytes_withOffsetAndLength_returnsSameHasherInstanceForChaining() {
        // Arrange: Create a concrete hasher instance and some input data.
        // We use a concrete implementation (Crc32cHasher) to test the abstract class's behavior.
        Hasher hasher = new Crc32cHasher();
        byte[] inputBytes = new byte[]{10, 20, 30};

        // Act: Call the putBytes method, which should return the hasher itself.
        Hasher returnedHasher = hasher.putBytes(inputBytes, 1, 1); // Process one byte from the middle

        // Assert: Verify that the returned object is the same instance as the original.
        // This confirms the method supports a fluent API (e.g., hasher.putA().putB().hash()).
        assertSame("The putBytes method must return the same instance to support method chaining.",
                hasher, returnedHasher);
    }
}