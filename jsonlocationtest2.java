package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link JsonLocation#toString()} method to ensure it produces
 * human-readable and accurate source descriptions for various input types.
 */
public class JsonLocationToStringTest extends JUnit5TestBase {

    // Constants for common location parameters to improve readability
    private static final int LINE_NUMBER = 1;
    private static final int COLUMN_NUMBER = 2;
    private static final long TOTAL_CHARS = 10L;
    private static final long TOTAL_BYTES = 10L;

    // A simple test class used as a source reference object
    private static class Foobar {}

    @Test
    void toStringWithNullSourceShouldDefaultToUnknown() {
        // Arrange
        // The JsonLocation constructor handles a null ContentReference by using ContentReference.unknown()
        JsonLocation location = new JsonLocation(null, 10L, 10L, 3, 2);
        String expected = "[Source: UNKNOWN; line: 3, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStringWithStringSourceShouldIncludeStringInDescription() {
        // Arrange
        ContentReference contentRef = createContentReference("string-source");
        JsonLocation location = new JsonLocation(contentRef, TOTAL_BYTES, TOTAL_CHARS, LINE_NUMBER, COLUMN_NUMBER);
        String expected = "[Source: (String)\"string-source\"; line: 1, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStringWithCharArraySourceShouldIncludeCharArrayInDescription() {
        // Arrange
        ContentReference contentRef = createContentReference("chars-source".toCharArray());
        JsonLocation location = new JsonLocation(contentRef, TOTAL_BYTES, TOTAL_CHARS, LINE_NUMBER, COLUMN_NUMBER);
        String expected = "[Source: (char[])\"chars-source\"; line: 1, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStringWithByteArraySourceShouldIncludeByteArrayInDescription() throws Exception {
        // Arrange
        ContentReference contentRef = createContentReference("bytes-source".getBytes("UTF-8"));
        JsonLocation location = new JsonLocation(contentRef, TOTAL_BYTES, TOTAL_CHARS, LINE_NUMBER, COLUMN_NUMBER);
        String expected = "[Source: (byte[])\"bytes-source\"; line: 1, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStringWithInputStreamSourceShouldIncludeStreamTypeInDescription() {
        // Arrange
        ContentReference contentRef = createContentReference(new ByteArrayInputStream(new byte[0]));
        JsonLocation location = new JsonLocation(contentRef, TOTAL_BYTES, TOTAL_CHARS, LINE_NUMBER, COLUMN_NUMBER);
        String expected = "[Source: (ByteArrayInputStream); line: 1, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStringWithClassAsSourceShouldIncludeClassNameInDescription() {
        // Arrange
        ContentReference contentRef = createRawContentReference(true, InputStream.class);
        JsonLocation location = new JsonLocation(contentRef, TOTAL_BYTES, TOTAL_CHARS, LINE_NUMBER, COLUMN_NUMBER);
        String expected = "[Source: (InputStream); line: 1, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toStringWithObjectAsSourceShouldIncludeObjectClassNameInDescription() {
        // Arrange
        Foobar sourceObject = new Foobar();
        ContentReference contentRef = createRawContentReference(true, sourceObject);
        JsonLocation location = new JsonLocation(contentRef, TOTAL_BYTES, TOTAL_CHARS, LINE_NUMBER, COLUMN_NUMBER);
        String expected = "[Source: (" + Foobar.class.getName() + "); line: 1, column: 2]";

        // Act
        String actual = location.toString();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    // --- Helper methods for creating ContentReference instances ---

    private ContentReference createContentReference(String rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length(), ErrorReportConfiguration.defaults());
    }

    private ContentReference createContentReference(char[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createContentReference(byte[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createContentReference(InputStream rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference createRawContentReference(boolean textual, Object rawSrc) {
        return ContentReference.rawReference(textual, rawSrc);
    }
}