package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Soundex} class.
 */
// Renamed from SoundexTestTest24 for clarity and to follow standard naming conventions.
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests that the static {@link Soundex#US_ENGLISH} instance correctly encodes a name.
     * This test case was added to address issues reported in CODEC-54 and CODEC-56.
     *
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-54">CODEC-54</a>
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-56">CODEC-56</a>
     */
    @Test
    // Renamed from testUsEnglishStatic to better describe the behavior being tested.
    void shouldEncodeNameUsingUsEnglishStaticInstance() {
        // Arrange
        final String nameToEncode = "Williams";
        final String expectedSoundex = "W452";

        // Act
        final String actualSoundex = Soundex.US_ENGLISH.soundex(nameToEncode);

        // Assert
        assertEquals(expectedSoundex, actualSoundex, "Encoding 'Williams' with US_ENGLISH Soundex should produce 'W452'");
    }
}