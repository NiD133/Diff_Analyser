package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its core functionality
 * like equality and hash code generation.
 */
class JsonLocationTest extends JUnit5TestBase {

    // This test class contains helper methods that are likely used by other tests
    // in a larger suite. They are refactored here for clarity and consistency.

    private void assertContentIsRedactedInException(JsonParseException e) {
        verifyException(e, "unrecognized token");
        JsonLocation location = e.getLocation();
        assertThat(location.contentReference().getRawContent()).isNull();
        assertThat(location.sourceDescription()).startsWith("REDACTED");
    }

    // --- Helper methods for creating ContentReference instances ---

    private ContentReference createSourceReference(String rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length(), ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(char[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(byte[] rawSrc) {
        return ContentReference.construct(true, rawSrc, 0, rawSrc.length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(byte[] rawSrc, int offset, int length) {
        return ContentReference.construct(true, rawSrc, offset, length, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(InputStream rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference createSourceReference(File rawSrc) {
        return ContentReference.construct(true, rawSrc, -1, -1, ErrorReportConfiguration.defaults());
    }

    private ContentReference createRawSourceReference(boolean textual, Object rawSrc) {
        return ContentReference.rawReference(textual, rawSrc);
    }

    // Unused in this specific test, but kept for completeness of the original class structure.
    static class Foobar {
    }

    @Test
    @DisplayName("equals() and hashCode() should follow their contracts")
    void equalsAndHashCodeContracts() {
        // Arrange
        final long TOTAL_CHARS = 10L;
        final int LINE_1 = 1;
        final int LINE_3 = 3;
        final int COLUMN = 2;
        final String SOURCE_TEXT = "src";

        ContentReference sourceRef = createSourceReference(SOURCE_TEXT);

        // A standard location with a source reference
        JsonLocation location1 = new JsonLocation(sourceRef, TOTAL_CHARS, TOTAL_CHARS, LINE_1, COLUMN);
        
        // An identical location to test equality and hashCode consistency
        JsonLocation location1Equivalent = new JsonLocation(sourceRef, TOTAL_CHARS, TOTAL_CHARS, LINE_1, COLUMN);

        // A different location (different line number and no explicit source reference)
        // Note: The constructor handles a null source by creating an "unknown" ContentReference.
        JsonLocation location2 = new JsonLocation(null, TOTAL_CHARS, TOTAL_CHARS, LINE_3, COLUMN);

        // Act & Assert

        // 1. Test 'equals()' contract
        assertThat(location1)
                .isEqualTo(location1) // Reflexive: an object equals itself
                .isEqualTo(location1Equivalent) // Symmetric: if a.equals(b), then b.equals(a)
                .isNotEqualTo(null) // Handles null comparison
                .isNotEqualTo(location2); // Not equal to a different object

        assertThat(location2).isNotEqualTo(location1);

        // 2. Test 'hashCode()' contract
        // If two objects are equal, they MUST have the same hash code.
        assertThat(location1.hashCode()).isEqualTo(location1Equivalent.hashCode());

        // A basic sanity check that non-trivial locations produce a non-zero hash code.
        assertThat(location1.hashCode()).isNotZero();
        assertThat(location2.hashCode()).isNotZero();
    }
}