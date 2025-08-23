package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the internal UTF-8 encoding logic in {@link UTF8Writer}.
 */
// Renamed from UTF8WriterTestTest6 for conciseness and clarity.
class UTF8WriterTest {

    /**
     * This test verifies the mathematical correctness of the optimized formula
     * for converting a UTF-16 surrogate pair into a 21-bit Unicode code point.
     * <p>
     * The standard formula is:
     * {@code codePoint = 0x10000 + ((high - 0xD800) << 10) + (low - 0xDC00)}
     * <p>
     * The optimized formula used in {@link UTF8Writer} is:
     * {@code codePoint = (high << 10) + low + SURROGATE_BASE}
     * <p>
     * This test exhaustively checks that both formulas produce the same result
     * for all valid surrogate pairs, thus validating the pre-calculated
     * {@link UTF8Writer#SURROGATE_BASE} constant.
     */
    @Test
    @DisplayName("Should correctly convert all valid surrogate pairs to code points using the optimized formula")
    void surrogatePairToCodePointConversion() {
        // Iterate through all valid high surrogates (U+D800 to U+DBFF)
        for (int highSurrogate = UTF8Writer.SURR1_FIRST; highSurrogate <= UTF8Writer.SURR1_LAST; highSurrogate++) {
            // Iterate through all valid low surrogates (U+DC00 to U+DFFF)
            for (int lowSurrogate = UTF8Writer.SURR2_FIRST; lowSurrogate <= UTF8Writer.SURR2_LAST; lowSurrogate++) {

                // Standard formula for converting a surrogate pair to a Unicode code point.
                int expectedCodePoint = 0x10000
                        + ((highSurrogate - UTF8Writer.SURR1_FIRST) << 10)
                        + (lowSurrogate - UTF8Writer.SURR2_FIRST);

                // Optimized formula used in UTF8Writer, relying on the SURROGATE_BASE constant.
                int actualCodePoint = (highSurrogate << 10) + lowSurrogate + UTF8Writer.SURROGATE_BASE;

                assertEquals(expectedCodePoint, actualCodePoint,
                        () -> String.format("Mismatch for surrogate pair (high: %#x, low: %#x)",
                                highSurrogate, lowSurrogate));
            }
        }
    }
}