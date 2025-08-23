package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RefinedSoundex}.
 */
// The class name has been improved for clarity and to follow standard naming conventions.
public class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    // The method name now clearly describes the specific scenario being tested.
    void shouldEncodeCorrectlyWhenConstructedWithMappingString() {
        // The test is structured using the Arrange-Act-Assert pattern for better readability.

        // Arrange: Set up the encoder and test data.
        // This test specifically verifies the constructor that accepts a mapping string.
        final RefinedSoundex soundexEncoder = new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING);
        final String input = "dogs";
        final String expectedEncoding = "D6043";

        // Act: Execute the method under test.
        final String actualEncoding = soundexEncoder.soundex(input);

        // Assert: Verify the result.
        assertEquals(expectedEncoding, actualEncoding, "Encoding 'dogs' with US English mapping should produce 'D6043'");
    }
}