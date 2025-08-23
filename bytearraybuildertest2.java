package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ByteArrayBuilderTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    /**
     * Verifies the lifecycle of a pooled {@link BufferRecycler}.
     * <p>
     * This test ensures that when a {@code BufferRecycler} is obtained from a pool,
     * it is correctly passed to and used by {@link ByteArrayBuilder} and {@link JsonGenerator},
     * and that it is not released back to the pool until explicitly done so by the
     * original creator.
     *
     * @see <a href="https://github.com/FasterXML/jackson-core/issues/1195">[core#1195]</a>
     */
    @Test
    void whenUsingPooledBufferRecycler_thenLifecycleIsManagedCorrectly() throws Exception {
        // --- Arrange ---
        final JsonFactory jsonFactory = new JsonFactory();
        // Create a BufferRecycler that is managed by a pool.
        final BufferRecycler pooledRecycler = new BufferRecycler()
                .withPool(JsonRecyclerPools.newBoundedPool(3));
        final int initialBuilderSize = 20;

        // --- Act & Assert ---

        // Phase 1: Use the recycler with a ByteArrayBuilder and a JsonGenerator
        try (ByteArrayBuilder builder = new ByteArrayBuilder(pooledRecycler, initialBuilderSize)) {
            // Verify the builder is using our specific recycler instance
            assertSame(pooledRecycler, builder.bufferRecycler(),
                    "ByteArrayBuilder should be constructed with the provided recycler.");

            try (JsonGenerator generator = jsonFactory.createGenerator(builder)) {
                // Access internal state to verify the generator also uses the same recycler
                IOContext ioContext = ((GeneratorBase) generator).ioContext();
                assertSame(pooledRecycler, ioContext.bufferRecycler(),
                        "JsonGenerator's IOContext should use the same recycler.");
                assertTrue(ioContext.bufferRecycler().isLinkedWithPool(),
                        "Recycler should be linked to a pool during generation.");

                // Perform a simple write operation
                generator.writeStartArray();
                generator.writeEndArray();
            } // generator.close() is automatically called here

            // Phase 2: Verify state after the generator is closed
            // The generator should NOT release the recycler back to the pool,
            // as the ByteArrayBuilder still owns it.
            assertTrue(pooledRecycler.isLinkedWithPool(),
                    "Recycler should remain linked to the pool after generator is closed.");

            // Retrieve the content and release the builder's internal buffers
            byte[] result = builder.getClearAndRelease();
            assertEquals("[]", new String(result, StandardCharsets.UTF_8));
        } // builder's resources are released here

        // Phase 3: Verify state after the builder is done
        // The ByteArrayBuilder also should NOT release the recycler to the pool.
        // The responsibility lies with the original creator of the recycler.
        assertTrue(pooledRecycler.isLinkedWithPool(),
                "Recycler should remain linked even after ByteArrayBuilder is done.");

        // Phase 4: Explicitly release the recycler back to the pool
        pooledRecycler.releaseToPool();
        assertFalse(pooledRecycler.isLinkedWithPool(),
                "Recycler should be unlinked only after being explicitly released to the pool.");
    }
}