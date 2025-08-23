package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the PhoneticEngine constructor throws an IllegalArgumentException
     * when initialized with the disallowed RuleType.RULES.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForRulesRuleType() {
        // Arrange: Define the invalid input for the constructor.
        // The RuleType.RULES is explicitly disallowed by the constructor's contract.
        final NameType nameType = NameType.ASHKENAZI;
        final RuleType invalidRuleType = RuleType.RULES;
        final boolean concatenate = false;

        // Act & Assert
        try {
            new PhoneticEngine(nameType, invalidRuleType, concatenate);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            final String expectedMessage = "ruleType must not be " + RuleType.RULES;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}