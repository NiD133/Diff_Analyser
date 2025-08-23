package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UTF8WriterTestTest3 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    @Test
    void flushAfterClose() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer w = new UTF8Writer(_ioContext(), out);
        w.write('X');
        char[] ch = { 'Y' };
        w.write(ch);
        w.close();
        assertEquals(2, out.size());
        // and this ought to be fine...
        w.flush();
        // as well as some more...
        w.close();
        w.flush();
    }
}