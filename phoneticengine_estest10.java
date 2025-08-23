package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * This test verifies the immutability of the PhoneticEngine.
     * The class documentation guarantees that the engine's state does not change after creation.
     * This test confirms that calling the encode() method does not alter the engine's
     * configuration properties.
     */
    @Test
    public void engineStateShouldRemainUnchangedAfterEncoding() {
        // Arrange: Create a PhoneticEngine with a specific configuration.
        final NameType nameType = NameType.ASHKENAZI;
        final RuleType ruleType = RuleType.EXACT;
        final boolean shouldConcat = false;
        final int maxPhonemes = 0;
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, shouldConcat, maxPhonemes);

        final Languages.LanguageSet emptyLanguageSet = Languages.LanguageSet.from(Collections.emptySet());
        final String sampleInput = "ben";

        // Act: Call the encode method. The return value is not relevant for this test,
        // as we are only checking for side effects (state mutation).
        engine.encode(sampleInput, emptyLanguageSet);

        // Assert: Verify that the engine's configuration has not been mutated by the encode() call.
        assertFalse("The 'concat' property should remain unchanged after encoding.", engine.isConcat());
        assertEquals("The 'maxPhonemes' property should remain unchanged after encoding.", maxPhonemes, engine.getMaxPhonemes());
    }
}