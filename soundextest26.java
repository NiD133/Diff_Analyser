package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Soundex} class.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests that the default US English mapping, which only supports standard English letters,
     * throws an exception for unmapped characters like 'ö'. The Soundex algorithm is
     * generally not defined for characters outside the A-Z range.
     *
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-30">CODEC-30</a>
     */
    @Test
    void shouldThrowExceptionWhenEncodingUnmappedCharacter() {
        final Soundex soundex = createStringEncoder();
        final String oWithDiaeresis = "\u00f6"; // The character 'ö'

        // The default Soundex mapping only includes A-Z.
        // Encoding a character outside this set should fail.
        assertThrows(IllegalArgumentException.class, () -> {
            soundex.encode(oWithDiaeresis);
        }, "Encoding a character not in the A-Z range should throw an IllegalArgumentException.");
    }
}