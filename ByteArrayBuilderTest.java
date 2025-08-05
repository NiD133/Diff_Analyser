package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its byte
 * aggregation and resource management capabilities.
 */
class ByteArrayBuilderTest extends com.fasterxml.jackson.core.JUnit5TestBase
{
    /**
     * Tests the basic functionality of the builder by writing bytes individually,
     * as single integers, and in chunks, then verifying the final aggregated byte array.
     */
    @Test
    void toByteArrayShouldCombineVariousWrites() {
        // Arrange
        // 1. Set up the expected result: a byte array containing values 0 through 99.
        byte[] expected = new byte[100];
        for (int i = 0; i < 100; ++i) {
            expected[i] = (byte) i;
        }

        // 2. Prepare a chunk of data to be written in bulk.
        byte[] dataChunk = new byte[98];
        for (int i = 0; i < dataChunk.length; ++i) {
            dataChunk[i] = (byte) (2 + i); // Values from 2 to 99
        }

        // Act
        byte[] actual;
        try (ByteArrayBuilder builder = new ByteArrayBuilder(null, 20)) {
            // Test initial state
            assertArrayEquals(new byte[0], builder.toByteArray(), "Initial state should be an empty byte array");

            // Write bytes using different methods to test aggregation
            builder.write((byte) 0);
            builder.append(1); // append(int) writes the low-order byte
            builder.write(dataChunk);

            actual = builder.toByteArray();
        } // builder.close() is automatically called, which also calls release()

        // Assert
        assertArrayEquals(expected, actual, "The aggregated byte array does not match the expected sequence.");
    }

    /**
     * Verifies that a {@link BufferRecycler} instance provided to the
     * {@link ByteArrayBuilder} is not released back to its pool when the builder
     * or a wrapping {@link JsonGenerator} is closed. The recycler should only be
     * released when explicitly managed.
     *
     * This behavior is critical for scenarios where a single BufferRecycler is
     * shared across multiple operations.
     *
     * Test for [core#1195].
     */
    @Test
    void bufferRecyclerShouldNotBeReleasedByBuilderOrGeneratorWhenExternallyManaged() throws Exception {
        // Arrange: Create a pooled BufferRecycler that we will manage manually.
        JsonFactory jsonFactory = new JsonFactory();
        BufferRecycler pooledRecycler = new BufferRecycler()
                .withPool(JsonRecyclerPools.newBoundedPool(3));

        // Act & Assert
        try (ByteArrayBuilder builder = new ByteArrayBuilder(pooledRecycler, 20)) {
            // The builder should use the provided recycler.
            assertSame(pooledRecycler, builder.bufferRecycler());

            // Create a JsonGenerator that writes to our builder.
            try (JsonGenerator generator = jsonFactory.createGenerator(builder)) {
                // The generator's IOContext should also use the same recycler.
                IOContext ioContext = ((GeneratorBase) generator).ioContext();
                assertSame(pooledRecycler, ioContext.bufferRecycler());
                assertTrue(ioContext.bufferRecycler().isLinkedWithPool(),
                        "Recycler should be linked to a pool at this stage.");

                generator.writeStartArray();
                generator.writeEndArray();
            } // generator.close() is called here.

            // Closing the generator should NOT release the externally managed recycler.
            assertTrue(pooledRecycler.isLinkedWithPool(),
                    "Generator.close() must not release an externally managed BufferRecycler.");

            // Retrieve the content from the builder.
            byte[] result = builder.getClearAndRelease();
            assertEquals("[]", new String(result, StandardCharsets.UTF_8));

        } // builder.close() is called here.

        // Closing the builder or accessing its contents should also NOT release the recycler.
        assertTrue(pooledRecycler.isLinkedWithPool(),
                "ByteArrayBuilder.close() must not release an externally managed BufferRecycler.");

        // Manually release the recycler back to the pool.
        pooledRecycler.releaseToPool();

        // The recycler should now be unlinked from the pool, indicating it has been returned.
        assertFalse(pooledRecycler.isLinkedWithPool(),
                "Recycler should be unlinked after explicit releaseToPool().");
    }
}