package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Soundex} class with the default US English mapping.
 */
public class SoundexTestTest25 extends AbstractStringEncoderTest<Soundex> {

    private Soundex soundex;

    @Override
    protected Soundex createStringEncoder() {
        // Creates a Soundex instance with the default US English mapping.
        return new Soundex();
    }

    @BeforeEach
    public void setUp() {
        this.soundex = createStringEncoder();
    }

    @Test
    @DisplayName("Should encode a basic ASCII letter using the default mapping")
    void testEncodeBasicAsciiLetter() {
        assertEquals("E000", this.soundex.encode("e"));
    }

    /**
     * The default US English mapping in Soundex only supports the 26 unaccented
     * letters of the alphabet. This test verifies that providing a character
     * outside this set, such as 'é', results in an exception.
     *
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-30">CODEC-30</a>
     */
    @Test
    @DisplayName("Should throw IllegalArgumentException for characters not in the US English mapping")
    void testEncodeThrowsExceptionForUnmappedCharacter() {
        final String inputWithDiacritic = "é"; // e-acute
        assertThrows(IllegalArgumentException.class,
                () -> this.soundex.encode(inputWithDiacritic),
                "Expected an exception for an unmapped character.");
    }
}