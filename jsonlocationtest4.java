package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on the
 * formatting of the source description.
 */
public class JsonLocationTestTest4 extends JUnit5TestBase {

    /**
     * Helper method to construct a {@link ContentReference} for a String source.
     * This simplifies test setup by encapsulating the construction logic.
     */
    private ContentReference sourceReferenceFor(String text) {
        return ContentReference.construct(
                /* isTextual */ true,
                text,
                /* offset */ 0,
                /* length */ text.length(),
                ErrorReportConfiguration.defaults());
    }

    /**
     * Tests that the {@link JsonLocation#sourceDescription()} method correctly
     * escapes non-printable characters like TAB and NULL.
     *
     * @see <a href="https://github.com/FasterXML/jackson-core/issues/658">jackson-core#658</a>
     */
    @Test
    void sourceDescriptionShouldEscapeNonPrintableCharacters() {
        // Arrange
        final String jsonWithNonPrintables = "[ \"tab:[\t]/null:[\0]\" ]";

        // For this test, we only care about the content reference. The other location
        // parameters (offsets, line/column numbers) are not relevant.
        final JsonLocation location = new JsonLocation(
                sourceReferenceFor(jsonWithNonPrintables),
                /* totalBytes */ -1L,
                /* totalChars */ -1L,
                /* lineNr */ -1,
                /* columnNr */ -1);

        // Act
        final String actualDescription = location.sourceDescription();

        // Assert
        // The expected description should be prefixed with the source type `(String)`,
        // wrapped in quotes, and have non-printable characters (TAB, NULL)
        // replaced with their Unicode escape sequences.
        final String expectedDescription = String.format("(String)\"[ \"tab:[%s]/null:[%s]\" ]\"",
                "\\u0009", "\\u0000");

        assertThat(actualDescription).isEqualTo(expectedDescription);
    }
}