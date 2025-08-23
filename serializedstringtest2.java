package com.fasterxml.jackson.core.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializedStringTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final String QUOTED = "\\\"quo\\\\ted\\\"";

    @Test
    void failedAccess() throws IOException {
        final String INPUT = "Bit longer text";
        SerializableString sstr = new SerializedString(INPUT);
        final byte[] buffer = new byte[INPUT.length() - 2];
        final char[] ch = new char[INPUT.length() - 2];
        final ByteBuffer bbuf = ByteBuffer.allocate(INPUT.length() - 2);
        assertEquals(-1, sstr.appendQuotedUTF8(buffer, 0));
        assertEquals(-1, sstr.appendQuoted(ch, 0));
        assertEquals(-1, sstr.putQuotedUTF8(bbuf));
        bbuf.rewind();
        assertEquals(-1, sstr.appendUnquotedUTF8(buffer, 0));
        assertEquals(-1, sstr.appendUnquoted(ch, 0));
        assertEquals(-1, sstr.putUnquotedUTF8(bbuf));
    }
}
