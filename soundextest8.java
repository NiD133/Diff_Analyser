package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link Soundex} encoder, focusing on its handling of special characters.
 */
@DisplayName("Soundex Encoder")
class SoundexTest {

    private Soundex soundex;

    @BeforeEach
    void setUp() {
        soundex = new Soundex();
    }

    @DisplayName("should ignore apostrophes when encoding")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @ValueSource(strings = {
        "OBrien",   // Base case without apostrophe
        "'OBrien",  // Apostrophe at the beginning
        "O'Brien",  // Apostrophe in the middle
        "OB'rien",  // Apostrophe in various middle positions
        "OBr'ien",
        "OBri'en",
        "OBrie'n",
        "OBrien'"   // Apostrophe at the end
    })
    void shouldEncodeIgnoringApostrophes(final String name) throws EncoderException {
        // Arrange
        final String expectedEncoding = "O165";

        // Act
        final String actualEncoding = soundex.encode(name);

        // Assert
        assertEquals(expectedEncoding, actualEncoding,
            "Apostrophes should be ignored, not affecting the Soundex code.");
    }
}