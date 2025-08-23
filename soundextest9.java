package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link Soundex} encoder, focusing on its handling of non-alphabetic characters like hyphens.
 */
public class SoundexTest {

    private Soundex soundexEncoder;

    @BeforeEach
    void setUp() {
        this.soundexEncoder = new Soundex();
    }

    /**
     * This parameterized test verifies that the Soundex implementation correctly ignores hyphens.
     * All variations of "KINGSMITH", regardless of where hyphens are placed, should produce the
     * same Soundex code. This approach makes each input an individual, verifiable test run.
     *
     * <p>The name "KINGSMITH" encodes to "K525" based on the Soundex algorithm:
     * <ul>
     *     <li><b>K</b>: Retained as the first letter.</li>
     *     <li><b>I</b>: Vowel, ignored.</li>
     *     <li><b>N</b>: Encoded as <b>5</b>.</li>
     *     <li><b>G</b>: Encoded as <b>2</b>.</li>
     *     <li><b>S</b>: Ignored (has the same code '2' as the previous consonant 'G').</li>
     *     <li><b>M</b>: Encoded as <b>5</b>.</li>
     *     <li><b>I</b>: Vowel, ignored.</li>
     *     <li><b>T</b>: Encoded as <b>3</b>.</li>
     *     <li><b>H</b>: Ignored.</li>
     * </ul>
     * The resulting code K-5-2-5-3 is truncated to 4 characters: "K525".</p>
     *
     * <p>Test data is sourced from http://www.myatt.demon.co.uk/sxalg.htm</p>
     *
     * @param nameVariant A variation of the name "KINGSMITH".
     * @throws EncoderException Not expected for these valid inputs.
     */
    @DisplayName("Soundex should ignore hyphens and encode variations of 'KINGSMITH' to K525")
    @ParameterizedTest(name = "input: {0}")
    @ValueSource(strings = {
        "KINGSMITH",    // Base case without hyphens
        "-KINGSMITH",   // Leading hyphen
        "K-INGSMITH",   // Internal hyphens
        "KI-NGSMITH",
        "KIN-GSMITH",
        "KING-SMITH",
        "KINGS-MITH",
        "KINGSM-ITH",
        "KINGSMI-TH",
        "KINGSMIT-H",
        "KINGSMITH-"    // Trailing hyphen
    })
    void shouldEncodeNameVariationsConsistently(final String nameVariant) throws EncoderException {
        final String expectedCode = "K525";
        assertEquals(expectedCode, this.soundexEncoder.encode(nameVariant));
    }
}