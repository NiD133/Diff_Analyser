package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UTF8WriterTestTest1 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    @Test
    void simple() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = new UTF8Writer(_ioContext(), out);
        String str = "AB\u00A0\u1AE9\uFFFC";
        char[] ch = str.toCharArray();
        // Let's write 3 times, using different methods
        w.write(str);
        w.append(ch[0]);
        w.write(ch[1]);
        w.write(ch, 2, 3);
        w.flush();
        w.write(str, 0, str.length());
        w.close();
        // and thus should have 3 times contents
        byte[] data = out.toByteArray();
        assertEquals(3 * 10, data.length);
        String act = utf8String(out);
        assertEquals(15, act.length());
        assertEquals(3 * str.length(), act.length());
        assertEquals(str + str + str, act);
    }
}
