package org.apache.commons.codec.language.bm;

import org.junit.Test;

/**
 * Unit tests for the {@link PhoneticEngine} class, focusing on exception handling.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the encode method throws a NullPointerException when called with a null LanguageSet.
     * The PhoneticEngine relies on a valid LanguageSet to process the input, and a null value
     * is an illegal argument that should result in an immediate failure.
     */
    @Test(expected = NullPointerException.class)
    public void encodeWithNullLanguageSetShouldThrowNullPointerException() {
        // Arrange: Create a PhoneticEngine with standard configuration.
        final NameType nameType = NameType.GENERIC;
        final RuleType ruleType = RuleType.EXACT;
        final boolean concat = false;
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat);

        final String input = "test";

        // Act: Call the encode method with a null LanguageSet.
        // This action is expected to throw a NullPointerException.
        engine.encode(input, (Languages.LanguageSet) null);

        // Assert: The test will pass if a NullPointerException is thrown, as declared
        // by the @Test(expected=...) annotation.
    }
}