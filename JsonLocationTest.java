package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for verifying the internal working of the {@link JsonLocation} class itself,
 * as opposed to the accuracy of reported location information by parsers.
 */
@DisplayName("JsonLocation Class Tests")
class JsonLocationTest extends JUnit5TestBase {

    // Helper class for testing with arbitrary object sources
    static class Foobar {
    }

    @Test
    @DisplayName("The equals() and hashCode() methods should follow their contract")
    void equalsAndHashCodeShouldFollowContract() {
        // given
        JsonLocation locationWithSource = new JsonLocation(createSourceReference("src"), 10L, 10L, 1, 2);
        JsonLocation locationWithoutSource = new JsonLocation(null, 10L, 10L, 3, 2);

        // then: standard equals() contract
        assertThat(locationWithSource)
                .isEqualTo(locationWithSource) // Reflexive
                .isNotEqualTo(null)             // Non-null
                .isNotEqualTo(new Object());    // Different type

        // then: locations with different properties should not be equal
        assertThat(locationWithSource).isNotEqualTo(locationWithoutSource);
        assertThat(locationWithoutSource).isNotEqualTo(locationWithSource); // Symmetric

        // then: hashCode() should be non-zero for valid locations
        assertThat(locationWithSource.hashCode()).isNotZero();
        assertThat(locationWithoutSource.hashCode()).isNotZero();
    }

    @Test
    @DisplayName("toString() should generate correct descriptions for various source types")
    void toStringShouldRenderCorrectlyForVariousSourceTypes() throws Exception {
        // given
        long totalBytes = 10L;
        long totalChars = 10L;
        int line = 1;
        int col = 2;

        // when/then: null source (defaults to UNKNOWN)
        JsonLocation locWithNullSource = new JsonLocation(null, totalBytes, totalChars, 3, 2);
        assertThat(locWithNullSource.toString()).isEqualTo("[Source: UNKNOWN; line: 3, column: 2]");

        // when/then: String source
        JsonLocation locWithStringSource = new JsonLocation(createSourceReference("string-source"), totalBytes, totalChars, line, col);
        assertThat(locWithStringSource.toString()).isEqualTo("[Source: (String)\"string-source\"; line: 1, column: 2]");

        // when/then: char[] source
        JsonLocation locWithCharSource = new JsonLocation(createSourceReference("chars-source".toCharArray()), totalBytes, totalChars, line, col);
        assertThat(locWithCharSource.toString()).isEqualTo("[Source: (char[])\"chars-source\"; line: 1, column: 2]");

        // when/then: byte[] source
        JsonLocation locWithByteSource = new JsonLocation(createSourceReference("bytes-source".getBytes("UTF-8")), totalBytes, totalChars, line, col);
        assertThat(locWithByteSource.toString()).isEqualTo("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]");

        // when/then: InputStream source
        JsonLocation locWithInputStreamSource = new JsonLocation(createSourceReference(new ByteArrayInputStream(new byte[0])), totalBytes, totalChars, line, col);
        assertThat(locWithInputStreamSource.toString()).isEqualTo("[Source: (ByteArrayInputStream); line: 1, column: 2]");

        // when/then: Class<?> source
        JsonLocation locWithClassSource = new JsonLocation(createRawSourceReference(true, InputStream.class), totalBytes, totalChars, line, col);
        assertThat(locWithClassSource.toString()).isEqualTo("[Source: (InputStream); line: 1, column: 2]");

        // when/then: Other Object source
        Foobar foobarSource = new Foobar();
        JsonLocation locWithObjectSource = new JsonLocation(createRawSourceReference(true, foobarSource), totalBytes, totalChars, line, col);
        String expectedDesc = String.format("[Source: (%s); line: 1, column: 2]", Foobar.class.getName());
        assertThat(locWithObjectSource.toString()).isEqualTo(expectedDesc);
    }

    @Test
    @DisplayName("sourceDescription() should truncate long String content")
    void sourceDescriptionShouldTruncateLongStringContent() {
        // given
        final int maxLength = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        final String prefix = "x".repeat(maxLength);
        final String suffix = "yyy";
        final String longContent = prefix + suffix;
        JsonLocation location = new JsonLocation(createSourceReference(longContent), 0L, 0L, 1, 1);

        // when
        String description = location.sourceDescription();

        // then
        String expected = String.format("(String)\"%s\"[truncated 3 chars]", prefix);
        assertThat(description).isEqualTo(expected);
    }

    @Test
    @DisplayName("sourceDescription() should truncate long byte[] content")
    void sourceDescriptionShouldTruncateLongByteArrayContent() throws Exception {
        // given
        final int maxLength = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        final String prefix = "x".repeat(maxLength);
        final String suffix = "yyy";
        final byte[] longContent = (prefix + suffix).getBytes("UTF-8");
        JsonLocation location = new JsonLocation(createSourceReference(longContent), 0L, 0L, 1, 1);

        // when
        String description = location.sourceDescription();

        // then
        String expected = String.format("(byte[])\"%s\"[truncated 3 bytes]", prefix);
        assertThat(description).isEqualTo(expected);
    }

    @Test
    @DisplayName("sourceDescription() should escape non-printable characters (issue #658)")
    void sourceDescriptionShouldEscapeNonPrintableCharacters() {
        // given
        final String contentWithUnprintableChars = "[ \"tab:[\t]/null:[\0]\" ]";
        JsonLocation location = new JsonLocation(createSourceReference(contentWithUnprintableChars), 0L, 0L, -1, -1);

        // when
        final String description = location.sourceDescription();

        // then
        final String expected = String.format("(String)\"[ \"tab:[%s]/null:[%s]\" ]\"", "\\u0009", "\\u0000");
        assertThat(description).isEqualTo(expected);
    }

    @DisplayName("Location should have redacted source when feature is disabled (issue #356)")
    @ParameterizedTest(name = "for {0} parser")
    @MethodSource("parserProviderForSourceInclusionTest")
    void locationShouldHaveRedactedSourceWhenDisabled(String-unusedParserType, ParserCreator parserCreator) {
        // given
        JsonFactory factory = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        // when
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            try (JsonParser p = parserCreator.create(factory)) {
                p.nextToken(); // consumes '['
                p.nextToken(); // throws on "foobar"
            }
        });

        // then
        assertSourceContentIsRedacted(exception);
    }

    // Helper interface for the parameterized test lambda
    @FunctionalInterface
    interface ParserCreator {
        JsonParser create(JsonFactory factory) throws Exception;
    }

    // Provides arguments for the parameterized test
    static Stream<Arguments> parserProviderForSourceInclusionTest() throws UnsupportedEncodingException {
        String json = "[ foobar ]";
        byte[] jsonBytes = json.getBytes("UTF-8");
        return Stream.of(
                Arguments.of("char-based", (ParserCreator) f -> f.createParser(json)),
                Arguments.of("byte-based", (ParserCreator) f -> f.createParser(jsonBytes))
        );
    }

    private void assertSourceContentIsRedacted(JsonParseException e) {
        verifyException(e, "unrecognized token");
        JsonLocation location = e.getLocation();
        assertThat(location.contentReference().getRawContent()).isNull();
        assertThat(location.sourceDescription()).startsWith("REDACTED");
    }

    @Nested
    @DisplayName("Equality checks with different source reference types (issue #739)")
    class EqualityWithSourceReferences {

        @Test
        @DisplayName("should be equal for locations with equal but distinct File sources")
        void shouldBeEqualForEqualFileSources() {
            // given: two distinct but equal File objects
            File sourceFile1 = new File("/tmp/foo");
            File sourceFile2 = new File("/tmp/foo");
            assertThat(sourceFile1).isEqualTo(sourceFile2).isNotSameAs(sourceFile2);

            // when
            JsonLocation location1 = new JsonLocation(createSourceReference(sourceFile1), 10L, 10L, 1, 2);
            JsonLocation location2 = new JsonLocation(createSourceReference(sourceFile2), 10L, 10L, 1, 2);

            // then
            assertThat(location1).isEqualTo(location2);
            assertThat(location1.hashCode()).isEqualTo(location2.hashCode());
        }

        @Test
        @DisplayName("should be equal for locations pointing to the same byte array segment")
        void shouldBeEqualForSameByteArraySegment() {
            // given
            final byte[] content = "BOGUS".getBytes();

            // when
            JsonLocation location1 = new JsonLocation(createSourceReference(content, 0, 5), 5L, 0L, 1, 2);
            JsonLocation location2 = new JsonLocation(createSourceReference(content, 0, 5), 5L, 0L, 1, 2);

            // then
            assertThat(location1).isEqualTo(location2);
            assertThat(location1.hashCode()).isEqualTo(location2.hashCode());
        }

        @Test
        @DisplayName("should not be equal for locations pointing to different byte array segments")
        void shouldNotBeEqualForDifferentByteArraySegments() {
            // given
            final byte[] content = "BOGUS".getBytes();

            // when
            JsonLocation location1 = new JsonLocation(createSourceReference(content, 0, 5), 5L, 0L, 1, 2);
            JsonLocation location2 = new JsonLocation(createSourceReference(content, 1, 4), 5L, 0L, 1, 2);

            // then
            assertThat(location1).isNotEqualTo(location2);
        }
    }

    // --- Helper methods for creating ContentReference instances ---

    private ContentReference createSourceReference(String rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length(),
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(char[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(byte[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(byte[] rawSrc, int offset, int length) {
        return ContentReference.construct(true, rawSrc, offset, length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(InputStream rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(File rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createRawSourceReference(boolean textual, Object rawSrc) {
        return ContentReference.rawReference(textual, rawSrc);
    }
}