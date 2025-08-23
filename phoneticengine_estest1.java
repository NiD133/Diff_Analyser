package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the three-argument constructor correctly initializes the engine's state,
     * specifically setting the 'concat' flag and using the default value for max phonemes.
     */
    @Test
    public void constructorShouldSetPropertiesAndUseDefaultMaxPhonemes() {
        // Arrange
        final NameType nameType = NameType.ASHKENAZI;
        final RuleType ruleType = RuleType.APPROX;
        final boolean shouldConcatenate = false;

        // Act
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, shouldConcatenate);

        // Assert
        // The 'concat' flag should reflect the value passed to the constructor.
        assertFalse("The 'concat' flag should be false.", engine.isConcat());

        // The three-argument constructor should use the default value for maxPhonemes.
        // As per the source code, this default is 20.
        assertEquals("Max phonemes should default to 20.", 20, engine.getMaxPhonemes());
    }
}