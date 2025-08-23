package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the PhoneticEngine class.
 * This specific test focuses on the behavior of the constructor.
 */
public class PhoneticEngine_ESTestTest31 extends PhoneticEngine_ESTest_scaffolding {

    /**
     * Tests that the 3-argument constructor correctly initializes the engine's properties
     * and sets the maximum number of phonemes to its default value.
     */
    @Test
    public void threeArgumentConstructorShouldSetPropertiesAndUseDefaultMaxPhonemes() {
        // Arrange: Define the configuration for the PhoneticEngine.
        final NameType expectedNameType = NameType.GENERIC;
        final RuleType expectedRuleType = RuleType.EXACT;
        final boolean expectedConcat = true;
        final int expectedDefaultMaxPhonemes = 20; // Default value from PhoneticEngine.DEFAULT_MAX_PHONEMES

        // Act: Create a new PhoneticEngine instance using the 3-argument constructor.
        final PhoneticEngine phoneticEngine = new PhoneticEngine(expectedNameType, expectedRuleType, expectedConcat);

        // Assert: Verify that all properties are initialized as expected.
        assertEquals("The NameType should match the constructor argument.",
                expectedNameType, phoneticEngine.getNameType());
        assertEquals("The RuleType should match the constructor argument.",
                expectedRuleType, phoneticEngine.getRuleType());
        assertTrue("The concat flag should be true as passed to the constructor.",
                phoneticEngine.isConcat());
        assertEquals("The maxPhonemes should be set to the default value.",
                expectedDefaultMaxPhonemes, phoneticEngine.getMaxPhonemes());
    }
}