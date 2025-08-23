package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the {@link PhoneticEngine} class, focusing on its configuration and property accessors.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the constructor correctly initializes the 'maxPhonemes' and 'concat' properties,
     * and that the corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldCorrectlySetMaxPhonemesAndConcatProperties() {
        // Arrange
        final NameType nameType = NameType.GENERIC;
        final RuleType ruleType = RuleType.EXACT;
        final boolean shouldConcat = false;
        final int expectedMaxPhonemes = 1;

        // Act
        final PhoneticEngine phoneticEngine = new PhoneticEngine(nameType, ruleType, shouldConcat, expectedMaxPhonemes);

        // Assert
        assertEquals("The max phonemes should be the value provided to the constructor.",
                     expectedMaxPhonemes, phoneticEngine.getMaxPhonemes());
        assertFalse("The concat flag should be the value provided to the constructor.",
                    phoneticEngine.isConcat());
    }
}