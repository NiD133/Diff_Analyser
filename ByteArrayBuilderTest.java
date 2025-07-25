package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayBuilderTest extends com.fasterxml.jackson.core.JUnit5TestBase
{
    @Test
    @DisplayName("Test simple byte array building")
    void testSimpleByteArrayBuilding() throws Exception
    {
        // Arrange: Create a ByteArrayBuilder with an initial capacity of 20 bytes.
        ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(null, 20);

        // Assert: Initially, the byte array should be empty.
        assertArrayEquals(new byte[0], byteArrayBuilder.toByteArray(), "Initial byte array should be empty.");

        // Act: Write some bytes to the builder.
        byteArrayBuilder.write((byte) 0);
        byteArrayBuilder.append(1);

        // Act: Write a larger byte array.
        byte[] data = new byte[98];
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) (2 + i);
        }
        byteArrayBuilder.write(data);

        // Act: Retrieve the built byte array.
        byte[] result = byteArrayBuilder.toByteArray();

        // Assert: Verify the size and contents of the built array.
        assertEquals(100, result.length, "Byte array should have a length of 100.");
        for (int i = 0; i < 100; ++i) {
            assertEquals(i, result[i], "Byte at index " + i + " should be " + i + ".");
        }

        // Act: Release resources.
        byteArrayBuilder.release();
        byteArrayBuilder.close();
    }

    @Test
    @DisplayName("Test BufferRecycler reuse")
    void testBufferRecyclerReuse() throws Exception
    {
        // Arrange: Create a JsonFactory and a BufferRecycler.
        JsonFactory jsonFactory = new JsonFactory();
        BufferRecycler bufferRecycler = new BufferRecycler()
                // need to link with some pool
                .withPool(JsonRecyclerPools.newBoundedPool(3));

        // Act & Assert: Use try-with-resources to ensure ByteArrayBuilder and JsonGenerator are closed.
        try (ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(bufferRecycler, 20)) {
            // Assert: The ByteArrayBuilder should use the provided BufferRecycler.
            assertSame(bufferRecycler, byteArrayBuilder.bufferRecycler(), "ByteArrayBuilder should use provided BufferRecycler.");

            try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(byteArrayBuilder)) {
                // Assert: The JsonGenerator's IOContext should also use the same BufferRecycler.
                IOContext ioContext = ((GeneratorBase) jsonGenerator).ioContext();
                assertSame(bufferRecycler, ioContext.bufferRecycler(), "IOContext should use the same BufferRecycler.");
                assertTrue(ioContext.bufferRecycler().isLinkedWithPool(), "BufferRecycler should be linked with pool.");

                // Act: Write an empty JSON array.
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }

            // Assert: Generator.close() should NOT release buffer recycler
            assertTrue(bufferRecycler.isLinkedWithPool(), "BufferRecycler should still be linked with pool after Generator close.");

            // Act: Get the cleared and released byte array.
            byte[] result = byteArrayBuilder.getClearAndRelease();
            assertEquals("[]", new String(result, StandardCharsets.UTF_8), "Result should be an empty JSON array.");
        }
        // Assert: Nor accessing contents
        assertTrue(bufferRecycler.isLinkedWithPool(), "BufferRecycler should still be linked with pool after accessing contents.");

        // Act: Explicitly release the BufferRecycler.
        bufferRecycler.releaseToPool();

        // Assert: The BufferRecycler should no longer be linked with the pool after explicit release.
        assertFalse(bufferRecycler.isLinkedWithPool(), "BufferRecycler should no longer be linked with pool after explicit release.");
    }
}