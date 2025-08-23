package org.jsoup.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Tokeniser}.
 * This test focuses on verifying internal data structures.
 */
public class TokeniserTest {

    private static final Charset WINDOWS_1252 = Charset.forName("Windows-1252");

    /**
     * Verifies that the hardcoded {@link Tokeniser#win1252Extensions} table is correct.
     * This table is a performance and compatibility optimization to handle illegal character
     * references by mapping them to Windows-1252 characters, as specified by the HTML standard.
     * This test ensures our hardcoded table matches the behavior of a standard JVM's
     * Windows-1252 charset, guaranteeing consistent parsing across platforms.
     */
    @Test
    @DisplayName("Ensures the hardcoded win1252Extensions table is correct")
    void verifiesWin1252ExtensionsTableIsCorrect() {
        for (int i = 0; i < Tokeniser.win1252Extensions.length; i++) {
            int byteValue = Tokeniser.win1252ExtensionsStart + i;
            int expectedCodepoint = Tokeniser.win1252Extensions[i];

            String decodedString = new String(new byte[]{(byte) byteValue}, WINDOWS_1252);

            // Some byte values in the 0x80-0x9F range are undefined control characters in Windows-1252.
            // The JVM decodes these to the Unicode replacement character (U+FFFD).
            // We skip these undefined characters in this verification test, as the Tokeniser's
            // character reference parsing logic handles them separately.
            if (decodedString.charAt(0) == Tokeniser.replacementChar) {
                continue;
            }

            assertEquals(1, decodedString.length(), "Each byte should decode to a single character.");

            int actualCodepoint = decodedString.codePointAt(0);

            assertEquals(expectedCodepoint, actualCodepoint,
                () -> String.format("Mismatch at index %d for byte 0x%X. Expected codepoint 0x%X, but got 0x%X.",
                    i, byteValue, expectedCodepoint, actualCodepoint));
        }
    }
}