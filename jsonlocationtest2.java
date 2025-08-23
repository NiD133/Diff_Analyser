package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JsonLocationTestTest2 extends JUnit5TestBase {

    private void _verifyContentDisabled(JsonParseException e) {
        verifyException(e, "unrecognized token");
        JsonLocation loc = e.getLocation();
        assertNull(loc.contentReference().getRawContent());
        assertThat(loc.sourceDescription()).startsWith("REDACTED");
    }

    private ContentReference _sourceRef(String rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length(), ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(char[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(byte[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(byte[] rawSrc, int offset, int length) {
        return ContentReference.construct(true, rawSrc, offset, length, ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(InputStream rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(File rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference _rawSourceRef(boolean textual, Object rawSrc) {
        return ContentReference.rawReference(textual, rawSrc);
    }

    static class Foobar {
    }

    @Test
    void basicToString() throws Exception {
        // no location; presumed to be Binary due to defaulting
        assertEquals("[Source: UNKNOWN; line: 3, column: 2]", new JsonLocation(null, 10L, 10L, 3, 2).toString());
        // Short String
        assertEquals("[Source: (String)\"string-source\"; line: 1, column: 2]", new JsonLocation(_sourceRef("string-source"), 10L, 10L, 1, 2).toString());
        // Short char[]
        assertEquals("[Source: (char[])\"chars-source\"; line: 1, column: 2]", new JsonLocation(_sourceRef("chars-source".toCharArray()), 10L, 10L, 1, 2).toString());
        // Short byte[]
        assertEquals("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]", new JsonLocation(_sourceRef("bytes-source".getBytes("UTF-8")), 10L, 10L, 1, 2).toString());
        // InputStream
        assertEquals("[Source: (ByteArrayInputStream); line: 1, column: 2]", new JsonLocation(_sourceRef(new ByteArrayInputStream(new byte[0])), 10L, 10L, 1, 2).toString());
        // Class<?> that specifies source type
        assertEquals("[Source: (InputStream); line: 1, column: 2]", new JsonLocation(_rawSourceRef(true, InputStream.class), 10L, 10L, 1, 2).toString());
        // misc other
        Foobar srcRef = new Foobar();
        assertEquals("[Source: (" + srcRef.getClass().getName() + "); line: 1, column: 2]", new JsonLocation(_rawSourceRef(true, srcRef), 10L, 10L, 1, 2).toString());
    }
}
