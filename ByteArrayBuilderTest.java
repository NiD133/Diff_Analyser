package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayBuilderTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    void testByteArrayBuilderBasicOperations() throws Exception {
        // Create a ByteArrayBuilder with an initial capacity of 20 bytes
        ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(null, 20);

        // Verify that the initial byte array is empty
        assertArrayEquals(new byte[0], byteArrayBuilder.toByteArray());

        // Write a single byte and append another byte
        byteArrayBuilder.write((byte) 0);
        byteArrayBuilder.append(1);

        // Create a byte array with values from 2 to 99
        byte[] byteArray = new byte[98];
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = (byte) (2 + i);
        }
        byteArrayBuilder.write(byteArray);

        // Verify the final byte array length and content
        byte[] result = byteArrayBuilder.toByteArray();
        assertEquals(100, result.length);
        for (int i = 0; i < 100; ++i) {
            assertEquals(i, result[i]);
        }

        // Release resources used by the ByteArrayBuilder
        byteArrayBuilder.release();
        byteArrayBuilder.close();
    }

    // Test to verify that BufferRecycler instance is reused
    @Test
    void testBufferRecyclerReuse() throws Exception {
        // Create a JsonFactory and a BufferRecycler with a bounded pool
        JsonFactory jsonFactory = new JsonFactory();
        BufferRecycler bufferRecycler = new BufferRecycler()
                .withPool(JsonRecyclerPools.newBoundedPool(3));

        // Use try-with-resources to ensure ByteArrayBuilder is closed
        try (ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(bufferRecycler, 20)) {
            // Verify that the ByteArrayBuilder uses the provided BufferRecycler
            assertSame(bufferRecycler, byteArrayBuilder.bufferRecycler());

            // Create a JsonGenerator and verify its IOContext uses the same BufferRecycler
            try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(byteArrayBuilder)) {
                IOContext ioContext = ((GeneratorBase) jsonGenerator).ioContext();
                assertSame(bufferRecycler, ioContext.bufferRecycler());
                assertTrue(ioContext.bufferRecycler().isLinkedWithPool());

                // Write an empty JSON array
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }

            // Verify that closing the generator does not release the buffer recycler
            assertTrue(bufferRecycler.isLinkedWithPool());

            // Verify the content of the ByteArrayBuilder
            byte[] result = byteArrayBuilder.getClearAndRelease();
            assertEquals("[]", new String(result, StandardCharsets.UTF_8));
        }

        // Verify that the buffer recycler is still linked with the pool
        assertTrue(bufferRecycler.isLinkedWithPool());

        // Explicitly release the buffer recycler to the pool
        bufferRecycler.releaseToPool();
        assertFalse(bufferRecycler.isLinkedWithPool());
    }
}