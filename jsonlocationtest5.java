package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.io.ContentReference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JsonLocationTestTest5 extends JUnit5TestBase {

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

    // for [jackson-core#356]
    @Test
    void disableSourceInclusion() throws Exception {
        JsonFactory f = JsonFactory.builder().disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION).build();
        try (JsonParser p = f.createParser("[ foobar ]")) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            try {
                p.nextToken();
                fail("Shouldn't have passed");
            } catch (JsonParseException e) {
                _verifyContentDisabled(e);
            }
        }
        // and verify same works for byte-based too
        try (JsonParser p = f.createParser("[ foobar ]".getBytes("UTF-8"))) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            try {
                p.nextToken();
                fail("Shouldn't have passed");
            } catch (JsonParseException e) {
                _verifyContentDisabled(e);
            }
        }
    }
}
