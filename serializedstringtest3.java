package com.fasterxml.jackson.core.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializedStringTestTest3 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final String QUOTED = "\\\"quo\\\\ted\\\"";

    @Test
    void testAppendQuotedUTF8() throws IOException {
        SerializedString sstr = new SerializedString(QUOTED);
        assertEquals(QUOTED, sstr.getValue());
        final byte[] buffer = new byte[100];
        final int len = sstr.appendQuotedUTF8(buffer, 3);
        assertEquals("\\\\\\\"quo\\\\\\\\ted\\\\\\\"", new String(buffer, 3, len));
    }
}
