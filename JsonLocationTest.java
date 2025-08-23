package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.io.ContentReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused tests for JsonLocation itself (not parser accuracy).
 * Emphasizes clarity with small helpers and Arrange-Act-Assert structure.
 */
class JsonLocationTest extends JUnit5TestBase {

    private static final long TEN = 10L;
    private static final int LINE_1 = 1;
    private static final int COL_2 = 2;

    private static final class Foobar { }

    // --------------------------------------------------------------------------------------
    // Basic equals/hashCode behavior
    // --------------------------------------------------------------------------------------

    @Test
    @DisplayName("equals/hashCode: self, null, different instances")
    void equalsAndHashCodeBasics() {
        JsonLocation loc1 = loc(refFrom("src"), TEN, TEN, 1, 2);
        JsonLocation loc2 = loc(nullRef(), TEN, TEN, 3, 2);

        // equals
        assertEquals(loc1, loc1);
        assertNotEquals(null, loc1);
        assertNotEquals(loc1, loc2);
        assertNotEquals(loc2, loc1);

        // hashCode is non-zero for populated locations
        assertThat(loc1.hashCode()).isNotZero();
        assertThat(loc2.hashCode()).isNotZero();
    }

    // --------------------------------------------------------------------------------------
    // toString / sourceDescription formatting
    // --------------------------------------------------------------------------------------

    @Test
    @DisplayName("toString: basic source types")
    void basicToString() {
        // Unknown (defaults to Binary)
        assertThat(loc(nullRef(), TEN, TEN, 3, 2).toString())
                .isEqualTo("[Source: UNKNOWN; line: 3, column: 2]");

        // Short String
        assertThat(loc(refFrom("string-source"), TEN, TEN, LINE_1, COL_2).toString())
                .isEqualTo("[Source: (String)\"string-source\"; line: 1, column: 2]");

        // Short char[]
        assertThat(loc(refFrom("chars-source".toCharArray()), TEN, TEN, LINE_1, COL_2).toString())
                .isEqualTo("[Source: (char[])\"chars-source\"; line: 1, column: 2]");

        // Short byte[]
        assertThat(loc(refFrom("bytes-source".getBytes(StandardCharsets.UTF_8)), TEN, TEN, LINE_1, COL_2).toString())
                .isEqualTo("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]");

        // InputStream
        assertThat(loc(refFrom(new ByteArrayInputStream(new byte[0])), TEN, TEN, LINE_1, COL_2).toString())
                .isEqualTo("[Source: (ByteArrayInputStream); line: 1, column: 2]");

        // Class<?> that specifies source type
        assertThat(loc(rawRef(true, InputStream.class), TEN, TEN, LINE_1, COL_2).toString())
                .isEqualTo("[Source: (InputStream); line: 1, column: 2]");

        // Misc other object
        Foobar srcRef = new Foobar();
        assertThat(loc(rawRef(true, srcRef), TEN, TEN, LINE_1, COL_2).toString())
                .isEqualTo("[Source: (" + srcRef.getClass().getName() + "); line: 1, column: 2]");
    }

    @Test
    @DisplayName("sourceDescription: truncates long sources and reports omitted size")
    void truncatedSource() {
        // Arrange
        int max = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        String main = "x".repeat(max);
        String json = main + "yyy";

        // Act / Assert (String)
        JsonLocation loc = loc(refFrom(json), 0L, 0L, 1, 1);
        assertThat(loc.sourceDescription())
                .isEqualTo(String.format("(String)\"%s\"[truncated 3 chars]", main));

        // Act / Assert (byte[])
        loc = loc(refFrom(json.getBytes(StandardCharsets.UTF_8)), 0L, 0L, 1, 1);
        assertThat(loc.sourceDescription())
                .isEqualTo(String.format("(byte[])\"%s\"[truncated 3 bytes]", main));
    }

    @Test
    @DisplayName("sourceDescription: escapes non-printable characters")
    void escapeNonPrintable() {
        String doc = "[ \"tab:[\t]/null:[\0]\" ]";

        JsonLocation loc = loc(refFrom(doc), 0L, 0L, -1, -1);
        assertThat(loc.sourceDescription())
                .isEqualTo(String.format("(String)\"[ \"tab:[%s]/null:[%s]\" ]\"", "\\u0009", "\\u0000"));
    }

    // --------------------------------------------------------------------------------------
    // Feature: INCLUDE_SOURCE_IN_LOCATION disabled
    // --------------------------------------------------------------------------------------

    @Test
    @DisplayName("INCLUDE_SOURCE_IN_LOCATION disabled: source redacted for char and byte input")
    void disableSourceInclusion() throws Exception {
        JsonFactory f = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        // Char-based
        try (JsonParser p = f.createParser("[ foobar ]")) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            JsonParseException e = assertThrows(JsonParseException.class, p::nextToken);
            verifySourceRedacted(e);
        }

        // Byte-based
        try (JsonParser p = f.createParser("[ foobar ]".getBytes(StandardCharsets.UTF_8))) {
            assertToken(JsonToken.START_ARRAY, p.nextToken());
            JsonParseException e = assertThrows(JsonParseException.class, p::nextToken);
            verifySourceRedacted(e);
        }
    }

    private void verifySourceRedacted(JsonParseException e) {
        verifyException(e, "unrecognized token");
        JsonLocation loc = e.getLocation();
        assertNull(loc.contentReference().getRawContent());
        assertThat(loc.sourceDescription()).startsWith("REDACTED");
    }

    // --------------------------------------------------------------------------------------
    // Equality across equal but distinct content references
    // --------------------------------------------------------------------------------------

    // for [jackson-core#739]
    @Test
    @DisplayName("equals: equal File references and equal byte ranges")
    void locationEquality() {
        // Equal but distinct File instances
        File src1 = new File("/tmp/foo");
        File src2 = new File("/tmp/foo");
        assertEquals(src1, src2);

        JsonLocation loc1 = loc(refFrom(src1), TEN, TEN, 1, 2);
        JsonLocation loc2 = loc(refFrom(src2), TEN, TEN, 1, 2);
        assertEquals(loc1, loc2);

        // Equal when same backing byte[] slice
        byte[] data = "BOGUS".getBytes(StandardCharsets.UTF_8);
        assertEquals(
                loc(refFrom(data, 0, 5), 5L, 0L, 1, 2),
                loc(refFrom(data, 0, 5), 5L, 0L, 1, 2)
        );

        // Not equal when byte slice differs
        JsonLocation a = loc(refFrom(data, 0, 5), 5L, 0L, 1, 2);
        JsonLocation b = loc(refFrom(data, 1, 4), 5L, 0L, 1, 2);
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // --------------------------------------------------------------------------------------
    // Small helpers for readability
    // --------------------------------------------------------------------------------------

    private static JsonLocation loc(ContentReference ref, long totalBytes, long totalChars, int line, int col) {
        return new JsonLocation(ref, totalBytes, totalChars, line, col);
    }

    private static ContentReference refFrom(String raw) {
        return ContentReference.construct(true, raw, 0, raw.length(), ErrorReportConfiguration.defaults());
    }

    private static ContentReference refFrom(char[] raw) {
        return ContentReference.construct(true, raw, 0, raw.length, ErrorReportConfiguration.defaults());
    }

    private static ContentReference refFrom(byte[] raw) {
        return ContentReference.construct(true, raw, 0, raw.length, ErrorReportConfiguration.defaults());
    }

    private static ContentReference refFrom(byte[] raw, int offset, int length) {
        return ContentReference.construct(true, raw, offset, length, ErrorReportConfiguration.defaults());
    }

    private static ContentReference refFrom(InputStream raw) {
        return ContentReference.construct(true, raw, -1, -1, ErrorReportConfiguration.defaults());
    }

    private static ContentReference refFrom(File raw) {
        return ContentReference.construct(true, raw, -1, -1, ErrorReportConfiguration.defaults());
    }

    private static ContentReference rawRef(boolean textual, Object raw) {
        return ContentReference.rawReference(textual, raw);
    }

    private static ContentReference nullRef() {
        return ContentReference.unknown();
    }
}