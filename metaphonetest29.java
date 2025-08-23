package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the specific encoding rules of the {@link Metaphone} class.
 */
// The class name is now clear, concise, and follows standard Java conventions.
// The unused helper methods have been removed to reduce clutter.
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * The Metaphone algorithm specifies that a 'B' is dropped if it appears after an 'M'
     * at the end of a word. This test verifies that rule with several examples.
     */
    @DisplayName("Metaphone should drop the trailing 'B' for words ending in 'MB'")
    @ParameterizedTest(name = "Encoding of \"{0}\" should be \"{1}\"")
    @CsvSource({
        "COMB, KM",
        "TOMB, TM",
        "WOMB, WM"
    })
    void testWordsEndingInMB(final String input, final String expectedEncoding) {
        // This single assertion now covers all test cases for this rule.
        // The test data is clearly separated from the test logic, improving readability.
        assertEquals(expectedEncoding, getStringEncoder().metaphone(input));
    }
}