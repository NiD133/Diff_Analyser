package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Verifies that the PhoneticEngine constructor correctly initializes the
     * 'maxPhonemes' and 'concat' properties based on the provided arguments.
     */
    @Test
    public void constructorShouldCorrectlySetMaxPhonemesAndConcatFlag() {
        // Arrange: Define the parameters for the PhoneticEngine constructor.
        final NameType nameType = NameType.ASHKENAZI;
        final RuleType ruleType = RuleType.EXACT;
        final boolean expectedConcat = false;
        final int expectedMaxPhonemes = 0;

        // Act: Create a new instance of the PhoneticEngine.
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, expectedConcat, expectedMaxPhonemes);

        // Assert: Check if the properties of the new instance were set correctly.
        assertFalse("The 'concat' flag should be set to false as passed to the constructor.",
                    engine.isConcat());
        assertEquals("The 'maxPhonemes' value should be set to 0 as passed to the constructor.",
                     expectedMaxPhonemes, engine.getMaxPhonemes());
    }
}