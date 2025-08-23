package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JsonLocationTestTest3 extends JUnit5TestBase {

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
    void truncatedSource() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH; ++i) {
            sb.append("x");
        }
        String main = sb.toString();
        String json = main + "yyy";
        JsonLocation loc = new JsonLocation(_sourceRef(json), 0L, 0L, 1, 1);
        String desc = loc.sourceDescription();
        assertEquals(String.format("(String)\"%s\"[truncated 3 chars]", main), desc);
        // and same with bytes
        loc = new JsonLocation(_sourceRef(json.getBytes("UTF-8")), 0L, 0L, 1, 1);
        desc = loc.sourceDescription();
        assertEquals(String.format("(byte[])\"%s\"[truncated 3 bytes]", main), desc);
    }
}
