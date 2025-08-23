package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    @Test
    public void constructorShouldCorrectlySetConfigurationProperties() {
        // Arrange
        final NameType nameType = NameType.SEPHARDIC;
        final RuleType ruleType = RuleType.APPROX;
        final boolean expectedConcat = true;
        final int expectedMaxPhonemes = 0;

        // Act
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, expectedConcat, expectedMaxPhonemes);

        // Assert
        // Verify that the getter methods return the values passed to the constructor.
        assertTrue("The 'concat' property should be true as set in the constructor.", engine.isConcat());
        assertEquals("The 'maxPhonemes' property should be 0 as set in the constructor.",
                     expectedMaxPhonemes, engine.getMaxPhonemes());
    }
}