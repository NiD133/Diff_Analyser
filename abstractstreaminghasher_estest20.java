package com.google.common.hash;

import static org.junit.Assert.assertSame;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Tests for the fluent API behavior of {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that putBytes(ByteBuffer) returns the same hasher instance,
     * which is essential for method chaining (fluent API).
     */
    @Test
    public void putBytes_withByteBuffer_returnsSameInstanceForChaining() {
        // Arrange
        // Crc32cHasher is a concrete implementation of the abstract class under test.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer inputBuffer = ByteBuffer.allocate(32);

        // Act
        Hasher returnedHasher = hasher.putBytes(inputBuffer);

        // Assert
        // The method should return `this` to allow for a fluent call chain,
        // e.g., hasher.putBytes(b1).putBytes(b2).hash().
        assertSame("The returned Hasher should be the same instance.", hasher, returnedHasher);
    }
}