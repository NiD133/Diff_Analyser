package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.io.ContentReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for verifying internal working of {@link JsonLocation} class itself,
 * as opposed to accuracy of reported location information by parsers.
 */
class JsonLocationTest extends JUnit5TestBase {
    static class Foobar { }

    // Helper methods for creating ContentReferences
    private ContentReference _sourceRef(String rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length(),
                ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(char[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(byte[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(byte[] rawSrc, int offset, int length) {
        return ContentReference.construct(true, rawSrc, offset, length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(InputStream rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference _sourceRef(File rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference _rawSourceRef(boolean textual, Object rawSrc) {
        return ContentReference.rawReference(textual, rawSrc);
    }

    // Test cases
    @Test
    void equals_shouldReturnTrueForSameInstance() {
        JsonLocation loc = new JsonLocation(_sourceRef("src"), 10L, 10L, 1, 2);
        assertThat(loc).isEqualTo(loc);
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        JsonLocation loc = new JsonLocation(_sourceRef("src"), 10L, 10L, 1, 2);
        assertThat(loc).isNotNull();
    }

    @Test
    void equals_shouldReturnFalseWhenSourcesDiffer() {
        JsonLocation loc1 = new JsonLocation(_sourceRef("src1"), 10L, 10L, 1, 2);
        JsonLocation loc2 = new JsonLocation(_sourceRef("src2"), 10L, 10L, 1, 2);
        assertThat(loc1).isNotEqualTo(loc2);
    }

    @Test
    void hashCode_shouldNotBeZero() {
        JsonLocation loc1 = new JsonLocation(_sourceRef("src"), 10L, 10L, 1, 2);
        JsonLocation loc2 = new JsonLocation(null, 10L, 10L, 3, 2);
        assertThat(loc1.hashCode()).isNotZero();
        assertThat(loc2.hashCode()).isNotZero();
    }

    @Test
    void toString_shouldHandleUnknownSource() {
        JsonLocation loc = new JsonLocation(null, 10L, 10L, 3, 2);
        assertThat(loc.toString()).isEqualTo("[Source: UNKNOWN; line: 3, column: 2]");
    }

    @Test
    void toString_shouldHandleStringSource() {
        JsonLocation loc = new JsonLocation(_sourceRef("string-source"), 10L, 10L, 1, 2);
        assertThat(loc.toString()).isEqualTo("[Source: (String)\"string-source\"; line: 1, column: 2]");
    }

    @Test
    void toString_shouldHandleCharArraySource() {
        JsonLocation loc = new JsonLocation(_sourceRef("chars-source".toCharArray()), 10L, 10L, 1, 2);
        assertThat(loc.toString()).isEqualTo("[Source: (char[])\"chars-source\"; line: 1, column: 2]");
    }

    @Test
    void toString_shouldHandleByteArraySource() throws Exception {
        JsonLocation loc = new JsonLocation(_sourceRef("bytes-source".getBytes("UTF-8")), 10L, 10L, 1, 2);
        assertThat(loc.toString()).isEqualTo("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]");
    }

    @Test
    void toString_shouldHandleInputStreamSource() {
        JsonLocation loc = new JsonLocation(_sourceRef(new ByteArrayInputStream(new byte[0])), 10L, 10L, 1, 2);
        assertThat(loc.toString()).isEqualTo("[Source: (ByteArrayInputStream); line: 1, column: 2]");
    }

    @Test
    void toString_shouldHandleSourceTypeClass() {
        JsonLocation loc = new JsonLocation(_rawSourceRef(true, InputStream.class), 10L, 10L, 1, 2);
        assertThat(loc.toString()).isEqualTo("[Source: (InputStream); line: 1, column: 2]");
    }

    @Test
    void toString_shouldHandleOtherSourceTypes() {
        Foobar srcRef = new Foobar();
        JsonLocation loc = new JsonLocation(_rawSourceRef(true, srcRef), 10L, 10L, 1, 2);
        assertThat(loc.toString()).isEqualTo("[Source: (" + srcRef.getClass().getName() + "); line: 1, column: 2]");
    }

    @Test
    void sourceDescription_shouldTruncateLongStrings() {
        int length = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('x');
        }
        String json = sb + "yyy";
        
        JsonLocation loc = new JsonLocation(_sourceRef(json), 0L, 0L, 1, 1);
        assertThat(loc.sourceDescription())
            .isEqualTo("(String)\"" + sb + "\"[truncated 3 chars]");
    }

    @Test
    void sourceDescription_shouldTruncateLongByteArrays() throws Exception {
        int length = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('x');
        }
        String json = sb + "yyy";
        
        JsonLocation loc = new JsonLocation(_sourceRef(json.getBytes("UTF-8")), 0L, 0L, 1, 1);
        assertThat(loc.sourceDescription())
            .isEqualTo("(byte[])\"" + sb + "\"[truncated 3 bytes]");
    }

    @Test
    void sourceDescription_shouldEscapeNonPrintableCharacters() {
        final String DOC = "[ \"tab:[\t]/null:[\0]\" ]";
        JsonLocation loc = new JsonLocation(_sourceRef(DOC), 0L, 0L, -1, -1);
        assertThat(loc.sourceDescription())
            .isEqualTo("(String)\"[ \"tab:[\\u0009]/null:[\\u0000]\" ]\"");
    }

    @Test
    void shouldDisableSourceInclusionForReader() throws Exception {
        JsonFactory f = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        try (JsonParser p = f.createParser("[ foobar ]")) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            assertThatThrownBy(p::nextToken)
                .isInstanceOf(JsonParseException.class)
                .satisfies(e -> verifyContentDisabled((JsonParseException) e));
        }
    }

    @Test
    void shouldDisableSourceInclusionForInputStream() throws Exception {
        JsonFactory f = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        try (JsonParser p = f.createParser("[ foobar ]".getBytes("UTF-8"))) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            assertThatThrownBy(p::nextToken)
                .isInstanceOf(JsonParseException.class)
                .satisfies(e -> verifyContentDisabled((JsonParseException) e));
        }
    }

    private void verifyContentDisabled(JsonParseException e) {
        verifyException(e, "unrecognized token");
        JsonLocation loc = e.getLocation();
        assertThat(loc.contentReference().getRawContent()).isNull();
        assertThat(loc.sourceDescription()).startsWith("REDACTED");
    }

    @Test
    void equals_shouldConsiderEqualFileReferencesEqual() {
        File src1 = new File("/tmp/foo");
        File src2 = new File("/tmp/foo");
        
        JsonLocation loc1 = new JsonLocation(_sourceRef(src1), 10L, 10L, 1, 2);
        JsonLocation loc2 = new JsonLocation(_sourceRef(src2), 10L, 10L, 1, 2);
        
        assertThat(loc1).isEqualTo(loc2);
    }

    @Test
    void equals_shouldConsiderSameByteArrayEqual() {
        final byte[] bogus = "BOGUS".getBytes();
        JsonLocation loc1 = new JsonLocation(_sourceRef(bogus, 0, 5), 5L, 0L, 1, 2);
        JsonLocation loc2 = new JsonLocation(_sourceRef(bogus, 0, 5), 5L, 0L, 1, 2);
        
        assertThat(loc1).isEqualTo(loc2);
    }

    @Test
    void equals_shouldDetectDifferentByteArrayOffsets() {
        final byte[] bogus = "BOGUS".getBytes();
        JsonLocation loc1 = new JsonLocation(_sourceRef(bogus, 0, 5), 5L, 0L, 1, 2);
        JsonLocation loc2 = new JsonLocation(_sourceRef(bogus, 1, 4), 5L, 0L, 1, 2);
        
        assertThat(loc1).isNotEqualTo(loc2);
    }
}