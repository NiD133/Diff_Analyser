package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UTF8WriterTestTest4 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    @Test
    void surrogatesOk() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = new UTF8Writer(_ioContext(), out);
        // First, valid case, char by char
        w.write(0xD83D);
        w.write(0xDE03);
        w.close();
        assertEquals(4, out.size());
        final byte[] EXP_SURROGATES = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83 };
        assertArrayEquals(EXP_SURROGATES, out.toByteArray());
        // and then as String
        out = new ByteArrayOutputStream();
        w = new UTF8Writer(_ioContext(), out);
        w.write("\uD83D\uDE03");
        w.close();
        assertEquals(4, out.size());
        assertArrayEquals(EXP_SURROGATES, out.toByteArray());
    }
}
