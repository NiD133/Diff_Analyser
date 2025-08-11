package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayBuilderTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final int FIRST_BLOCK_SIZE = 20;
    private static final int TOTAL_EXPECTED_BYTES = 100;

    private static byte[] sequentialBytes(int startInclusive, int count) {
        byte[] out = new byte[count];
        for (int i = 0; i < count; i++) {
            out[i] = (byte) (startInclusive + i);
        }
        return out;
    }

    @Test
    void toByteArray_isEmptyWhenNoWrites() {
        ByteArrayBuilder builder = new ByteArrayBuilder(null, FIRST_BLOCK_SIZE);
        try {
            // When no content has been written
            byte[] actual = builder.toByteArray();

            // Then result is an empty array
            assertArrayEquals(new byte[0], actual, "Empty builder should return empty array");
        } finally {
            // Ensure resources are released in case recycler is used
            builder.release();
            builder.close();
        }
    }

    @Test
    void write_singleBytesAndArray_yieldsContiguousSequence() {
        ByteArrayBuilder builder = new ByteArrayBuilder(null, FIRST_BLOCK_SIZE);
        try {
            // Arrange: write 0 and 1 using two different APIs
            builder.write((byte) 0);
            builder.append(1);

            // ...then write bytes 2..99 as a block
            builder.write(sequentialBytes(2, 98));

            // Act
            byte[] actual = builder.toByteArray();

            // Assert
            assertEquals(TOTAL_EXPECTED_BYTES, actual.length, "Unexpected total number of bytes");
            assertArrayEquals(sequentialBytes(0, TOTAL_EXPECTED_BYTES), actual,
                    "Should contain bytes 0..99 in order");
        } finally {
            builder.release();
            builder.close();
        }
    }

    // [core#1195]: Verify that BufferRecycler instance is reused and requires explicit release
    @Test
    void bufferRecycler_isReusedAndOnlyExplicitlyReleased() throws Exception {
        // Given a JsonFactory and a recycler linked to a bounded pool
        JsonFactory factory = new JsonFactory();
        BufferRecycler recycler = new BufferRecycler()
                .withPool(JsonRecyclerPools.newBoundedPool(3));

        try (ByteArrayBuilder builder = new ByteArrayBuilder(recycler, FIRST_BLOCK_SIZE)) {
            assertSame(recycler, builder.bufferRecycler(), "Builder must expose the provided recycler");

            // When a JsonGenerator writes into the builder
            try (JsonGenerator gen = factory.createGenerator(builder)) {
                IOContext ioCtxt = ((GeneratorBase) gen).ioContext();

                assertSame(recycler, ioCtxt.bufferRecycler(), "Generator should share the same recycler");
                assertTrue(ioCtxt.bufferRecycler().isLinkedWithPool(),
                        "Recycler should be linked to the pool while generator is open");

                gen.writeStartArray();
                gen.writeEndArray();
            }

            // Then closing the generator should NOT release the recycler
            assertTrue(recycler.isLinkedWithPool(), "Closing generator must NOT release the recycler");

            // And accessing/clearing the builder's content should NOT release it either
            byte[] json = builder.getClearAndRelease();
            assertEquals("[]", new String(json, StandardCharsets.UTF_8), "Unexpected JSON payload");
        }

        // Closing the builder must still NOT release the recycler
        assertTrue(recycler.isLinkedWithPool(),
                "Closing builder or accessing its contents must not release the recycler");

        // Only an explicit release detaches it from the pool
        recycler.releaseToPool();
        assertFalse(recycler.isLinkedWithPool(), "Explicit release must detach recycler from the pool");
    }
}