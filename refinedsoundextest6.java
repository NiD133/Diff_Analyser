package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    @DisplayName("An instance created with a custom char array mapping should encode strings correctly")
    void shouldEncodeCorrectlyWhenInitializedWithCharArrayMapping() {
        // Arrange: Create a RefinedSoundex instance using the constructor that accepts a char array.
        // We use the standard US English mapping to verify this constructor works as expected.
        final char[] mapping = RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray();
        final RefinedSoundex soundexEncoder = new RefinedSoundex(mapping);
        final String input = "dogs";
        final String expectedEncoding = "D6043";

        // Act: Encode the input string using the custom encoder.
        final String actualEncoding = soundexEncoder.soundex(input);

        // Assert: The encoded string should match the expected value.
        assertEquals(expectedEncoding, actualEncoding, "Encoding with a char[]-initialized encoder should produce the correct Refined Soundex code.");
    }
}