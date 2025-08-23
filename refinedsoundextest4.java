package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link RefinedSoundex} encoder.
 */
// Renamed class for clarity and to follow standard naming conventions.
public class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    /**
     * This is a characterization test (also known as a golden master test).
     * It verifies that the encoding of a string containing all 256 extended ASCII
     * characters (from \u0000 to \u00FF) produces a specific, known-good result.
     * This protects against unintentional changes in the encoder's behavior across
     * a wide range of inputs.
     *
     * <p>The expected string is derived as follows:</p>
     * <ol>
     *   <li>The encoder filters the input, retaining only alphabetic characters.</li>
     *   <li>The first letter found is 'A' (\u0041), which becomes the first character of the code.</li>
     *   <li>The encoder then processes the uppercase English alphabet ('A'-'Z'), generating the first part of the code:
     *       {@code "013602404378015936020505"}.</li>
     *   <li>It then processes the lowercase English alphabet ('a'-'z'), which appends an identical sequence:
     *       {@code "013602404378015936020505"}.</li>
     *   <li>Finally, it processes the German Eszett 'ß' (\u00DF). {@code Character.toUpperCase('ß')}
     *       returns "SS". The first 'S' (code '3') is appended because it differs from the
     *       previous character's code ('z' -> '5'). The second 'S' is ignored as a duplicate.</li>
     * </ol>
     * This results in the final expected code.
     */
    @Test
    void testEncodeAllExtendedAsciiCharacters() {
        // Arrange: Create an input string containing all characters from \u0000 to \u00FF.
        final char[] allChars = new char[256];
        for (int i = 0; i < allChars.length; i++) {
            allChars[i] = (char) i;
        }
        final String input = new String(allChars);

        final String expectedEncoding = "A0136024043780159360205050136024043780159360205053";

        // Act: Encode the comprehensive input string.
        final String actualEncoding = this.stringEncoder.encode(input);

        // Assert: The actual encoding must match the known-good "golden" value.
        assertEquals(expectedEncoding, actualEncoding);
    }
}