package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for {@link PhoneticEngine}.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the encode method throws an IllegalArgumentException when provided with a language
     * for which no phonetic rules are defined.
     */
    @Test
    public void encodeShouldThrowIllegalArgumentExceptionForLanguageWithNoRules() {
        // ARRANGE
        // Create an engine configured for generic names and exact rule matching.
        final PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);

        // Define a language that has no associated phonetic rules.
        final String nonExistentLanguage = "nolang";
        final Set<String> languageNames = Collections.singleton(nonExistentLanguage);
        final Languages.LanguageSet languageSet = Languages.LanguageSet.from(languageNames);

        final String anyInput = "any name";

        // ACT & ASSERT
        try {
            engine.encode(anyInput, languageSet);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (final IllegalArgumentException e) {
            // The engine is expected to fail when it cannot find the rules for the given language.
            // It constructs a resource name like "gen_rules_nolang.txt" and fails when it's not found.
            final String expectedMessage = "No rules found for gen, rules, " + nonExistentLanguage + ".";
            assertEquals("The exception message did not match the expected format.", expectedMessage, e.getMessage());
        }
    }
}