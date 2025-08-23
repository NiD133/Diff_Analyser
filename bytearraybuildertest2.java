package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import static org.junit.jupiter.api.Assertions.*;

public class ByteArrayBuilderTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    // [core#1195]: Try to verify that BufferRecycler instance is indeed reused
    @Test
    void bufferRecyclerReuse() throws Exception {
        JsonFactory f = new JsonFactory();
        BufferRecycler br = new BufferRecycler().// need to link with some pool
        withPool(JsonRecyclerPools.newBoundedPool(3));
        try (ByteArrayBuilder bab = new ByteArrayBuilder(br, 20)) {
            assertSame(br, bab.bufferRecycler());
            try (JsonGenerator g = f.createGenerator(bab)) {
                IOContext ioCtxt = ((GeneratorBase) g).ioContext();
                assertSame(br, ioCtxt.bufferRecycler());
                assertTrue(ioCtxt.bufferRecycler().isLinkedWithPool());
                g.writeStartArray();
                g.writeEndArray();
            }
            // Generator.close() should NOT release buffer recycler
            assertTrue(br.isLinkedWithPool());
            byte[] result = bab.getClearAndRelease();
            assertEquals("[]", new String(result, StandardCharsets.UTF_8));
        }
        // Nor accessing contents
        assertTrue(br.isLinkedWithPool());
        // only explicit release does
        br.releaseToPool();
        assertFalse(br.isLinkedWithPool());
    }
}
