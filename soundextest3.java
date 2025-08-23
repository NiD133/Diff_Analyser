package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Soundex#difference(String, String)} method.
 *
 * <p>The difference algorithm is based on the number of matching Soundex codes
 * between two strings. The result ranges from 0 (no similarity) to 4 (strong similarity).
 * </p>
 */
public class SoundexTestTest3 extends AbstractStringEncoderTest<Soundex> {

    private Soundex soundex;

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @BeforeEach
    public void setUp() {
        soundex = createStringEncoder();
    }

    @Test
    void testDifferenceOnEdgeCases() throws EncoderException {
        // The difference is 0 for null, empty, or blank strings.
        assertEquals(0, soundex.difference(null, null));
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundex.difference(" ", " "));
    }

    @Test
    void testDifferenceOnSampleNames() throws EncoderException {
        // 4 indicates strong similarity or identical codes.
        assertEquals(4, soundex.difference("Smith", "Smythe"));

        // 2 indicates some phonetic similarity.
        assertEquals(2, soundex.difference("Ann", "Andrew"));

        // 1 indicates weak phonetic similarity.
        assertEquals(1, soundex.difference("Margaret", "Andrew"));

        // 0 indicates little or no similarity.
        assertEquals(0, soundex.difference("Janet", "Margaret"));
    }

    @Test
    void testDifferenceMatchesTsqlExamples() throws EncoderException {
        // These test cases are based on examples from the Microsoft T-SQL documentation
        // for its DIFFERENCE function, ensuring compatibility.
        // Source 1: https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
        assertEquals(4, soundex.difference("Green", "Greene"));
        assertEquals(0, soundex.difference("Blotchet-Halls", "Greene"));

        // Source 2: https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
        assertEquals(4, soundex.difference("Smith", "Smythe"));
        assertEquals(4, soundex.difference("Smithers", "Smythers"));
        assertEquals(2, soundex.difference("Anothers", "Brothers"));
    }
}