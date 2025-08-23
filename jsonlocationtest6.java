package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JsonLocationTestTest6 extends JUnit5TestBase {

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

    // for [jackson-core#739]: try to support equality
    @Test
    void locationEquality() throws Exception {
        // important: create separate but equal instances
        File src1 = new File("/tmp/foo");
        File src2 = new File("/tmp/foo");
        assertEquals(src1, src2);
        JsonLocation loc1 = new JsonLocation(_sourceRef(src1), 10L, 10L, 1, 2);
        JsonLocation loc2 = new JsonLocation(_sourceRef(src2), 10L, 10L, 1, 2);
        assertEquals(loc1, loc2);
        // Also make sure to consider offset/length
        final byte[] bogus = "BOGUS".getBytes();
        // If same, equals:
        assertEquals(new JsonLocation(_sourceRef(bogus, 0, 5), 5L, 0L, 1, 2), new JsonLocation(_sourceRef(bogus, 0, 5), 5L, 0L, 1, 2));
        // If different, not equals
        loc1 = new JsonLocation(_sourceRef(bogus, 0, 5), 5L, 0L, 1, 2);
        loc2 = new JsonLocation(_sourceRef(bogus, 1, 4), 5L, 0L, 1, 2);
        assertNotEquals(loc1, loc2);
        assertNotEquals(loc2, loc1);
    }
}