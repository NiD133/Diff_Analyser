package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayBuilderTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    private static final int INITIAL_SIZE = 20;
    private static final int SINGLE_BYTE = 0;
    private static final int APPENDED_BYTE = 1;
    private static final int FILL_START = 2;
    private static final int FILL_LENGTH = 98;
    private static final int EXPECTED_TOTAL_LENGTH = 100;

    @Test
    void initialBuilderStateIsEmpty() {
        try (ByteArrayBuilder builder = new ByteArrayBuilder(null, INITIAL_SIZE)) {
            assertArrayEquals(new byte[0], builder.toByteArray(),
                "New builder should contain empty byte array");
        }
    }

    @Test
    void writeAndAppendOperationsAddBytes() throws Exception {
        try (ByteArrayBuilder builder = new ByteArrayBuilder(null, INITIAL_SIZE)) {
            // Write bytes using different methods
            builder.write((byte) SINGLE_BYTE);
            builder.append(APPENDED_BYTE);
            
            byte[] currentSegment = builder.getCurrentSegment();
            int segmentLength = builder.getCurrentSegmentLength();
            byte[] result = new byte[segmentLength];
            System.arraycopy(currentSegment, 0, result, 0, segmentLength);
            
            assertArrayEquals(new byte[]{SINGLE_BYTE, APPENDED_BYTE}, result,
                "First two bytes should match written values");
        }
    }

    @Test
    void writeByteArrayAggregatesContentCorrectly() throws Exception {
        try (ByteArrayBuilder builder = new ByteArrayBuilder(null, INITIAL_SIZE)) {
            // Write initial bytes
            builder.write((byte) SINGLE_BYTE);
            builder.append(APPENDED_BYTE);
            
            // Write dynamically generated array
            byte[] generatedArray = generateConsecutiveBytes(FILL_START, FILL_LENGTH);
            builder.write(generatedArray);
            
            // Verify full content
            byte[] result = builder.toByteArray();
            assertEquals(EXPECTED_TOTAL_LENGTH, result.length,
                "Aggregated content should have correct length");
            
            byte[] expectedContent = generateConsecutiveBytes(0, EXPECTED_TOTAL_LENGTH);
            assertArrayEquals(expectedContent, result,
                "Aggregated content should match expected sequence");
        }
    }

    // Helper method to generate consecutive byte values
    private byte[] generateConsecutiveBytes(int start, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (start + i);
        }
        return bytes;
    }

    // [core#1195]: Verify BufferRecycler instance reuse
    @Test
    void bufferRecyclerIsReusedThroughoutLifecycle() throws Exception {
        final int POOL_BOUND = 3;
        final String EXPECTED_JSON = "[]";
        final byte[] EXPECTED_BYTES = EXPECTED_JSON.getBytes(StandardCharsets.UTF_8);
        
        JsonFactory factory = new JsonFactory();
        BufferRecycler recycler = new BufferRecycler()
            .withPool(JsonRecyclerPools.newBoundedPool(POOL_BOUND));

        try (ByteArrayBuilder builder = new ByteArrayBuilder(recycler, INITIAL_SIZE)) {
            assertSame(recycler, builder.bufferRecycler(),
                "Builder should use the provided BufferRecycler");
            
            try (JsonGenerator generator = factory.createGenerator(builder)) {
                IOContext ioContext = ((GeneratorBase) generator).ioContext();
                assertSame(recycler, ioContext.bufferRecycler(),
                    "Generator's IOContext should share BufferRecycler");
                assertTrue(ioContext.bufferRecycler().isLinkedWithPool(),
                    "BufferRecycler should be linked to pool");
                
                // Write minimal JSON content
                generator.writeStartArray();
                generator.writeEndArray();
            }
            
            // Verify recycler remains linked after generator close
            assertTrue(recycler.isLinkedWithPool(),
                "BufferRecycler should remain linked after generator close");
            
            // Access content and release explicitly
            byte[] result = builder.getClearAndRelease();
            assertEquals(EXPECTED_JSON, new String(result, StandardCharsets.UTF_8),
                "Generated JSON should match expected content");
        }
        
        // Verify recycler remains linked after builder close
        assertTrue(recycler.isLinkedWithPool(),
            "BufferRecycler should remain linked after accessing content");
        
        // Explicit release breaks pool link
        recycler.releaseToPool();
        assertFalse(recycler.isLinkedWithPool(),
            "Explicit release should break pool linkage");
    }
}