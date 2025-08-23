package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the constructor correctly initializes the engine's properties
     * and that the corresponding getters return the expected values.
     */
    @Test
    public void constructorShouldSetPropertiesCorrectly() {
        // Arrange
        final NameType nameType = NameType.ASHKENAZI;
        final RuleType ruleType = RuleType.EXACT;
        final boolean shouldConcat = true;
        final int expectedMaxPhonemes = -516; // The constructor currently accepts negative values.

        // Act
        final PhoneticEngine phoneticEngine = new PhoneticEngine(nameType, ruleType, shouldConcat, expectedMaxPhonemes);

        // Assert
        // Verify that the properties set in the constructor are returned by the getters.
        assertEquals("The maxPhonemes property should match the value from the constructor.",
                     expectedMaxPhonemes, phoneticEngine.getMaxPhonemes());
        assertTrue("The concat property should match the value from the constructor.",
                   phoneticEngine.isConcat());
    }
}