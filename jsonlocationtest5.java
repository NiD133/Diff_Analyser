package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This test suite verifies the behavior of {@link JsonLocation} when the
 * {@link StreamReadFeature#INCLUDE_SOURCE_IN_LOCATION} feature is disabled.
 *
 * @see JsonLocation
 * @see StreamReadFeature#INCLUDE_SOURCE_IN_LOCATION
 */
public class JsonLocationTestTest5 extends JUnit5TestBase {

    private JsonFactory factoryWithSourceDisabled;

    @BeforeEach
    void setUp() {
        // This factory is configured to disable the inclusion of source content
        // in JsonLocation upon errors. This is the main configuration under test.
        factoryWithSourceDisabled = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
    }

    @Test
    @DisplayName("Source content should be redacted for String input when feature is disabled")
    void shouldRedactSourceForStringInputWhenDisabled() {
        // GIVEN
        String invalidJson = "[ foobar ]";

        // WHEN
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            try (JsonParser parser = factoryWithSourceDisabled.createParser(invalidJson)) {
                parser.nextToken(); // Consume '['
                parser.nextToken(); // This call is expected to fail on the invalid token 'foobar'
            }
        });

        // THEN
        assertSourceContentIsRedacted(exception);
    }

    @Test
    @DisplayName("Source content should be redacted for byte array input when feature is disabled")
    void shouldRedactSourceForByteArrayInputWhenDisabled() throws IOException {
        // GIVEN
        byte[] invalidJson = "[ foobar ]".getBytes("UTF-8");

        // WHEN
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            try (JsonParser parser = factoryWithSourceDisabled.createParser(invalidJson)) {
                parser.nextToken(); // Consume '['
                parser.nextToken(); // This call is expected to fail on the invalid token 'foobar'
            }
        });

        // THEN
        assertSourceContentIsRedacted(exception);
    }

    /**
     * Asserts that the {@link JsonLocation} from a {@link JsonParseException}
     * does not contain the original source content.
     *
     * @param e The exception to inspect.
     */
    private void assertSourceContentIsRedacted(JsonParseException e) {
        // First, verify the exception's cause is what we expect.
        verifyException(e, "unrecognized token");

        // Then, verify the location information has been properly redacted.
        JsonLocation location = e.getLocation();
        ContentReference contentRef = location.contentReference();

        // The raw content should not be available in the content reference.
        assertThat(contentRef.getRawContent()).isNull();

        // The source description should be explicitly marked as "REDACTED".
        assertThat(location.sourceDescription()).startsWith("REDACTED");
    }
}