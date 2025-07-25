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
    void testByteArrayBuilderInitializationAndWriteOperations() throws Exception {
        // Initialize ByteArrayBuilder with a null BufferRecycler and initial size of 20
        ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(null, 20);
        
        // Verify that the initial byte array is empty
        assertArrayEquals(new byte[0], byteArrayBuilder.toByteArray());

        // Write single bytes to the builder
        byteArrayBuilder.write((byte) 0);
        byteArrayBuilder.append(1);

        // Create a byte array with values from 2 to 99
        byte[] byteArray = new byte[98];
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = (byte) (2 + i);
        }
        // Write the byte array to the builder
        byteArrayBuilder.write(byteArray);

        // Convert the builder's content to a byte array and verify its length and content
        byte[] result = byteArrayBuilder.toByteArray();
        assertEquals(100, result.length);
        for (int i = 0; i < 100; ++i) {
            assertEquals(i, result[i]);
        }

        // Release resources used by the builder
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

        // Use try-with-resources to ensure ByteArrayBuilder is closed properly
        try (ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(bufferRecycler, 20)) {
            // Verify that the ByteArrayBuilder uses the provided BufferRecycler
            assertSame(bufferRecycler, byteArrayBuilder.bufferRecycler());

            // Create a JsonGenerator using the ByteArrayBuilder
            try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(byteArrayBuilder)) {
                IOContext ioContext = ((GeneratorBase) jsonGenerator).ioContext();
                
                // Verify that the IOContext uses the same BufferRecycler
                assertSame(bufferRecycler, ioContext.bufferRecycler());
                assertTrue(ioContext.bufferRecycler().isLinkedWithPool());

                // Write an empty JSON array
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }

            // Ensure that closing the generator does not release the buffer recycler
            assertTrue(bufferRecycler.isLinkedWithPool());

            // Get the content from the builder and verify it matches the expected JSON
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