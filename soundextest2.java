package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Contains specific tests for the {@link Soundex} class.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Test
    @DisplayName("Soundex encoding should ignore non-alphabetic characters")
    void shouldIgnoreNonAlphabeticCharacters() {
        // The Soundex algorithm is defined for US-English letters. This test
        // verifies that characters outside the A-Z range are ignored by the encoder.
        // The encoding of "HOL>MES" should be identical to the encoding of "HOLMES".

        // Arrange
        final Soundex soundexEncoder = createStringEncoder();
        final String inputWithNonLetter = "HOL>MES";
        final String expectedEncoding = "H452"; // This is the encoding for "HOLMES"

        // Act
        final String actualEncoding = soundexEncoder.encode(inputWithNonLetter);

        // Assert
        assertEquals(expectedEncoding, actualEncoding,
            "Non-alphabetic characters should be ignored during encoding.");
    }
}