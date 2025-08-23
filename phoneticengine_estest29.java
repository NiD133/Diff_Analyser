package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Verifies that the three-argument constructor correctly initializes the engine's
     * properties (NameType, RuleType, and concatenation flag) and that it correctly
     * applies the default value for the maximum number of phonemes.
     */
    @Test
    public void constructorShouldSetPropertiesAndUseDefaultMaxPhonemes() {
        // Arrange
        final NameType expectedNameType = NameType.SEPHARDIC;
        final RuleType expectedRuleType = RuleType.APPROX;
        final boolean expectedConcat = false;
        final int expectedDefaultMaxPhonemes = 20; // As defined by DEFAULT_MAX_PHONEMES in the source

        // Act
        final PhoneticEngine engine = new PhoneticEngine(expectedNameType, expectedRuleType, expectedConcat);

        // Assert
        assertEquals("The name type should match the constructor argument.",
                expectedNameType, engine.getNameType());
        assertEquals("The rule type should match the constructor argument.",
                expectedRuleType, engine.getRuleType());
        assertFalse("The concat flag should be false as per the constructor argument.",
                engine.isConcat());
        assertEquals("The max phonemes should be set to the default value.",
                expectedDefaultMaxPhonemes, engine.getMaxPhonemes());
    }
}