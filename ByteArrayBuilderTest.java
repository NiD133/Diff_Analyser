package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ByteArrayBuilder functionality including basic operations
 * and buffer recycling behavior.
 */
class ByteArrayBuilderTest extends com.fasterxml.jackson.core.JUnit5TestBase
{
    private static final int INITIAL_BUFFER_SIZE = 20;
    private static final int BUFFER_POOL_SIZE = 3;
    
    /**
     * Tests basic ByteArrayBuilder operations: writing individual bytes,
     * appending values, writing byte arrays, and verifying the final result.
     */
    @Test
    void testBasicByteArrayBuilderOperations() throws Exception
    {
        // Given: A new ByteArrayBuilder with initial capacity
        ByteArrayBuilder builder = new ByteArrayBuilder(null, INITIAL_BUFFER_SIZE);
        
        // Then: Initially should be empty
        assertArrayEquals(new byte[0], builder.toByteArray());

        // When: Writing first two bytes (0 and 1)
        builder.write((byte) 0);
        builder.append(1);

        // When: Creating and writing a large byte array (values 2-99)
        byte[] largeByteArray = createSequentialByteArray(98, 2);
        builder.write(largeByteArray);

        // Then: Result should contain all 100 bytes in sequence (0-99)
        byte[] result = builder.toByteArray();
        assertSequentialByteArray(result, 100, 0);

        // Cleanup
        builder.release();
        builder.close();
    }

    /**
     * Tests that BufferRecycler instances are properly reused across
     * ByteArrayBuilder and JsonGenerator operations.
     * 
     * This addresses issue [core#1195] regarding BufferRecycler reuse.
     */
    @Test
    void testBufferRecyclerIsProperlyReused() throws Exception
    {
        // Given: A JsonFactory and BufferRecycler with bounded pool
        JsonFactory jsonFactory = new JsonFactory();
        BufferRecycler bufferRecycler = createBufferRecyclerWithPool();

        try (ByteArrayBuilder builder = new ByteArrayBuilder(bufferRecycler, INITIAL_BUFFER_SIZE)) {
            // Then: Builder should use the same BufferRecycler instance
            assertSame(bufferRecycler, builder.bufferRecycler());

            // When: Creating JsonGenerator with the builder
            try (JsonGenerator generator = jsonFactory.createGenerator(builder)) {
                // Then: Generator should use the same BufferRecycler
                verifyGeneratorUsesBufferRecycler(generator, bufferRecycler);
                
                // When: Writing JSON content
                writeEmptyJsonArray(generator);
            }

            // Then: BufferRecycler should still be linked after generator closes
            assertTrue(bufferRecycler.isLinkedWithPool(), 
                "BufferRecycler should remain linked after generator close");

            // When: Getting content and releasing builder
            byte[] jsonContent = builder.getClearAndRelease();
            
            // Then: Content should be valid JSON and BufferRecycler still linked
            assertEquals("[]", new String(jsonContent, StandardCharsets.UTF_8));
        }
        
        // Then: BufferRecycler should still be linked after builder closes
        assertTrue(bufferRecycler.isLinkedWithPool(), 
            "BufferRecycler should remain linked after builder close");

        // When: Explicitly releasing to pool
        bufferRecycler.releaseToPool();
        
        // Then: BufferRecycler should no longer be linked
        assertFalse(bufferRecycler.isLinkedWithPool(), 
            "BufferRecycler should be unlinked after explicit release");
    }

    // Helper methods for better readability and reusability
    
    private BufferRecycler createBufferRecyclerWithPool() {
        return new BufferRecycler()
            .withPool(JsonRecyclerPools.newBoundedPool(BUFFER_POOL_SIZE));
    }
    
    private byte[] createSequentialByteArray(int length, int startValue) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) (startValue + i);
        }
        return array;
    }
    
    private void assertSequentialByteArray(byte[] actual, int expectedLength, int startValue) {
        assertEquals(expectedLength, actual.length, 
            "Array should have expected length");
        
        for (int i = 0; i < expectedLength; i++) {
            assertEquals(startValue + i, actual[i], 
                String.format("Byte at index %d should be %d", i, startValue + i));
        }
    }
    
    private void verifyGeneratorUsesBufferRecycler(JsonGenerator generator, BufferRecycler expectedRecycler) {
        IOContext ioContext = ((GeneratorBase) generator).ioContext();
        assertSame(expectedRecycler, ioContext.bufferRecycler(), 
            "Generator should use the same BufferRecycler instance");
        assertTrue(ioContext.bufferRecycler().isLinkedWithPool(), 
            "BufferRecycler should be linked with pool");
    }
    
    private void writeEmptyJsonArray(JsonGenerator generator) throws Exception {
        generator.writeStartArray();
        generator.writeEndArray();
    }
}