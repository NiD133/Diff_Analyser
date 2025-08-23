package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link PhoneticEngine} constructor.
 * Note: The class name and inheritance structure are preserved from the original
 * auto-generated test.
 */
public class PhoneticEngine_ESTestTest8 extends PhoneticEngine_ESTest_scaffolding {

    /**
     * Tests that the PhoneticEngine constructor throws an IllegalArgumentException
     * when the provided RuleType is RULES, as this is an explicitly disallowed value.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenRuleTypeIsRules() {
        // Arrange: The RuleType.RULES is an invalid argument for the constructor.
        // The other parameters can be any valid value, as they are not under test.
        final NameType anyNameType = NameType.SEPHARDIC;
        final RuleType invalidRuleType = RuleType.RULES;
        final boolean anyConcat = true;
        final int anyMaxPhonemes = 10;
        final String expectedMessage = "ruleType must not be " + RuleType.RULES;

        try {
            // Act: Attempt to create a PhoneticEngine with the invalid RuleType.
            new PhoneticEngine(anyNameType, invalidRuleType, anyConcat, anyMaxPhonemes);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (final IllegalArgumentException e) {
            // Assert: Verify that the caught exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}