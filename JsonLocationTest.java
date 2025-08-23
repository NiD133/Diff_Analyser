package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.io.ContentReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for verifying internal working of {@link JsonLocation} class itself,
 * as opposed to accuracy of reported location information by parsers.
 */
class JsonLocationTest extends JUnit5TestBase {

    static class Foobar { }

    @Test
    void testJsonLocationEqualityAndHashCode() {
        JsonLocation location1 = new JsonLocation(createSourceReference("src"), 10L, 10L, 1, 2);
        JsonLocation location2 = new JsonLocation(null, 10L, 10L, 3, 2);

        assertEquals(location1, location1, "A JsonLocation should be equal to itself");
        assertNotEquals(null, location1, "A JsonLocation should not be equal to null");
        assertNotEquals(location1, location2, "Different JsonLocations should not be equal");
        assertNotEquals(location2, location1, "Equality should be symmetric");

        assertTrue(location1.hashCode() != 0, "Hash code should not be zero");
        assertTrue(location2.hashCode() != 0, "Hash code should not be zero");
    }

    @Test
    void testJsonLocationToString() throws Exception {
        assertEquals("[Source: UNKNOWN; line: 3, column: 2]",
                new JsonLocation(null, 10L, 10L, 3, 2).toString(),
                "Default source should be UNKNOWN");

        assertEquals("[Source: (String)\"string-source\"; line: 1, column: 2]",
                new JsonLocation(createSourceReference("string-source"), 10L, 10L, 1, 2).toString(),
                "Source should be correctly represented for String");

        assertEquals("[Source: (char[])\"chars-source\"; line: 1, column: 2]",
                new JsonLocation(createSourceReference("chars-source".toCharArray()), 10L, 10L, 1, 2).toString(),
                "Source should be correctly represented for char[]");

        assertEquals("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]",
                new JsonLocation(createSourceReference("bytes-source".getBytes("UTF-8")), 10L, 10L, 1, 2).toString(),
                "Source should be correctly represented for byte[]");

        assertEquals("[Source: (ByteArrayInputStream); line: 1, column: 2]",
                new JsonLocation(createSourceReference(new ByteArrayInputStream(new byte[0])), 10L, 10L, 1, 2).toString(),
                "Source should be correctly represented for InputStream");

        assertEquals("[Source: (InputStream); line: 1, column: 2]",
                new JsonLocation(createRawSourceReference(true, InputStream.class), 10L, 10L, 1, 2).toString(),
                "Source should be correctly represented for InputStream class");

        Foobar sourceReference = new Foobar();
        assertEquals("[Source: (" + sourceReference.getClass().getName() + "); line: 1, column: 2]",
                new JsonLocation(createRawSourceReference(true, sourceReference), 10L, 10L, 1, 2).toString(),
                "Source should be correctly represented for custom object");
    }

    @Test
    void testTruncatedSourceDescription() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH; ++i) {
            builder.append("x");
        }
        String mainContent = builder.toString();
        String extendedContent = mainContent + "yyy";

        JsonLocation location = new JsonLocation(createSourceReference(extendedContent), 0L, 0L, 1, 1);
        String description = location.sourceDescription();
        assertEquals(String.format("(String)\"%s\"[truncated 3 chars]", mainContent), description,
                "Source description should indicate truncation for String");

        location = new JsonLocation(createSourceReference(extendedContent.getBytes("UTF-8")), 0L, 0L, 1, 1);
        description = location.sourceDescription();
        assertEquals(String.format("(byte[])\"%s\"[truncated 3 bytes]", mainContent), description,
                "Source description should indicate truncation for byte[]");
    }

    @Test
    void testEscapeNonPrintableCharacters() throws Exception {
        final String document = "[ \"tab:[\t]/null:[\0]\" ]";
        JsonLocation location = new JsonLocation(createSourceReference(document), 0L, 0L, -1, -1);
        final String sourceDescription = location.sourceDescription();
        assertEquals(String.format("(String)\"[ \"tab:[%s]/null:[%s]\" ]\"", "\\u0009", "\\u0000"), sourceDescription,
                "Non-printable characters should be escaped");
    }

    @Test
    void testDisableSourceInclusion() throws Exception {
        JsonFactory factory = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        try (JsonParser parser = factory.createParser("[ foobar ]")) {
            assertToken(JsonToken.START_ARRAY, parser.nextToken());
            try {
                parser.nextToken();
                fail("Parser should throw exception for unrecognized token");
            } catch (JsonParseException e) {
                verifyContentDisabled(e);
            }
        }

        try (JsonParser parser = factory.createParser("[ foobar ]".getBytes("UTF-8"))) {
            assertToken(JsonToken.START_ARRAY, parser.nextToken());
            try {
                parser.nextToken();
                fail("Parser should throw exception for unrecognized token");
            } catch (JsonParseException e) {
                verifyContentDisabled(e);
            }
        }
    }

    private void verifyContentDisabled(JsonParseException exception) {
        verifyException(exception, "unrecognized token");
        JsonLocation location = exception.getLocation();
        assertNull(location.contentReference().getRawContent(), "Raw content should be null when source inclusion is disabled");
        assertThat(location.sourceDescription()).startsWith("REDACTED", "Source description should be redacted");
    }

    @Test
    void testJsonLocationEquality() throws Exception {
        File sourceFile1 = new File("/tmp/foo");
        File sourceFile2 = new File("/tmp/foo");
        assertEquals(sourceFile1, sourceFile2, "Files with the same path should be equal");

        JsonLocation location1 = new JsonLocation(createSourceReference(sourceFile1), 10L, 10L, 1, 2);
        JsonLocation location2 = new JsonLocation(createSourceReference(sourceFile2), 10L, 10L, 1, 2);
        assertEquals(location1, location2, "JsonLocations with the same source and offsets should be equal");

        final byte[] bogusData = "BOGUS".getBytes();

        assertEquals(new JsonLocation(createSourceReference(bogusData, 0, 5), 5L, 0L, 1, 2),
                new JsonLocation(createSourceReference(bogusData, 0, 5), 5L, 0L, 1, 2),
                "JsonLocations with the same byte source and offsets should be equal");

        location1 = new JsonLocation(createSourceReference(bogusData, 0, 5), 5L, 0L, 1, 2);
        location2 = new JsonLocation(createSourceReference(bogusData, 1, 4), 5L, 0L, 1, 2);
        assertNotEquals(location1, location2, "JsonLocations with different byte offsets should not be equal");
        assertNotEquals(location2, location1, "Equality should be symmetric");
    }

    private ContentReference createSourceReference(String rawSource) {
        return ContentReference.construct(true, rawSource, 0, rawSource.length(), ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(char[] rawSource) {
        return ContentReference.construct(true, rawSource, 0, rawSource.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(byte[] rawSource) {
        return ContentReference.construct(true, rawSource, 0, rawSource.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(byte[] rawSource, int offset, int length) {
        return ContentReference.construct(true, rawSource, offset, length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(InputStream rawSource) {
        return ContentReference.construct(true, rawSource, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(File rawSource) {
        return ContentReference.construct(true, rawSource, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference createRawSourceReference(boolean textual, Object rawSource) {
        return ContentReference.rawReference(textual, rawSource);
    }
}