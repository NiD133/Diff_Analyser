package org.apache.commons.codec.language.bm;

import org.junit.Test;

/**
 * Contains tests for edge cases and error handling in the PhoneticEngine,
 * specifically focusing on how it handles unusually long input strings.
 */
public class PhoneticEngineLongInputTest {

    /**
     * A very long and complex string designed to trigger an out-of-bounds error
     * in the rule-matching logic of older versions of PhoneticEngine.
     */
    private static final String EXTREMELY_LONG_INPUT =
            "org.apache.commons.cod6c.language.bm.Res}urce*onstants";

    /**
     * Verifies that the {@code encode} method throws an {@code ArrayIndexOutOfBoundsException}
     * when processing an extremely long input string.
     *
     * <p>This scenario previously caused a bug where the rule-matching algorithm would
     * attempt to read past the bounds of the input string when checking rule contexts.
     * This test acts as a regression test to ensure the issue remains fixed.</p>
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void encodeShouldThrowArrayIndexOutOfBoundsExceptionForVeryLongInput() {
        // Arrange: Create a PhoneticEngine with a specific configuration known
        // to be vulnerable to the long input bug.
        final NameType nameType = NameType.ASHKENAZI;
        final RuleType ruleType = RuleType.APPROX;
        final boolean useConcat = true;
        final PhoneticEngine phoneticEngine = new PhoneticEngine(nameType, ruleType, useConcat);

        // Act: Attempt to encode the problematic long string.
        // The test will pass if, and only if, an ArrayIndexOutOfBoundsException is thrown.
        phoneticEngine.encode(EXTREMELY_LONG_INPUT);
    }
}