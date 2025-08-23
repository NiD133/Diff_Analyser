package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UTF8WriterTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    @Test
    void simpleAscii() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = new UTF8Writer(_ioContext(), out);
        String str = "abcdefghijklmnopqrst\u00A0";
        char[] ch = str.toCharArray();
        w.write(ch, 0, ch.length);
        // trigger different code path for close
        w.flush();
        w.close();
        byte[] data = out.toByteArray();
        // one 2-byte encoded char
        assertEquals(ch.length + 1, data.length);
        String act = utf8String(out);
        assertEquals(str, act);
    }
}
