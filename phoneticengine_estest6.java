package org.apache.commons.codec.language.bm;

import org.junit.Test;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Tests that calling encode() with a null input string results in a NullPointerException.
     * This is the expected behavior for methods that do not explicitly handle null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void encodeShouldThrowNullPointerExceptionForNullInput() {
        // Arrange: Create a PhoneticEngine with a standard configuration.
        // The specific configuration does not affect the outcome for a null input.
        NameType nameType = NameType.SEPHARDIC;
        RuleType ruleType = RuleType.EXACT;
        boolean concatenate = true;
        PhoneticEngine phoneticEngine = new PhoneticEngine(nameType, ruleType, concatenate);

        // Act & Assert: Call encode with null, which is expected to throw the exception.
        phoneticEngine.encode(null);
    }
}