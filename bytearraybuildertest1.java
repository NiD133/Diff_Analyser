package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its byte-appending and array-building capabilities.
 */
public class ByteArrayBuilderTestTest1 extends com.fasterxml.jackson.core.JUnit5TestBase {

    /**
     * Tests the builder's ability to correctly construct a byte array
     * by combining single-byte appends and whole-array writes.
     */
    @Test
    void shouldBuildByteArrayFromMixedWriteAndAppendOperations() {
        // Arrange: Prepare the data we expect to see.
        // The final byte array should contain values from 0 to 99.
        final byte[] expectedResult = new byte[100];
        for (int i = 0; i < expectedResult.length; i++) {
            expectedResult[i] = (byte) i;
        }

        // This is the chunk of data we will write after the first two bytes.
        // It contains values from 2 to 99.
        final byte[] dataChunk = new byte[98];
        for (int i = 0; i < dataChunk.length; i++) {
            dataChunk[i] = (byte) (i + 2);
        }

        // Use try-with-resources to ensure the builder is automatically closed
        // and its internal buffers are released, even if assertions fail.
        try (ByteArrayBuilder builder = new ByteArrayBuilder(null, 20)) {
            // Assert initial state: A new builder should be empty.
            assertEquals(0, builder.size());
            assertArrayEquals(new byte[0], builder.toByteArray(), "A new builder should produce an empty byte array.");

            // Act: Perform a series of write and append operations.
            builder.write((byte) 0);
            builder.append(1); // append(int) is specified to write the low-order byte.
            builder.write(dataChunk);

            // Assert: Verify the final constructed byte array is correct.
            byte[] actualResult = builder.toByteArray();
            assertArrayEquals(expectedResult, actualResult, "The final byte array should match the expected sequence.");
        }
    }
}