package com.fasterxml.jackson.core.util;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import static org.junit.jupiter.api.Assertions.*;

public class ByteArrayBuilderTestTest1 extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    void simple() throws Exception {
        ByteArrayBuilder b = new ByteArrayBuilder(null, 20);
        assertArrayEquals(new byte[0], b.toByteArray());
        b.write((byte) 0);
        b.append(1);
        byte[] foo = new byte[98];
        for (int i = 0; i < foo.length; ++i) {
            foo[i] = (byte) (2 + i);
        }
        b.write(foo);
        byte[] result = b.toByteArray();
        assertEquals(100, result.length);
        for (int i = 0; i < 100; ++i) {
            assertEquals(i, result[i]);
        }
        b.release();
        b.close();
    }
}
