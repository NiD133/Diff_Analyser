package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the inner class {@link PhoneticEngine.PhonemeBuilder}.
 */
public class PhoneticEngine_PhonemeBuilderTest {

    /**
     * Tests that applying a rule with an empty phoneme to an empty builder
     * does not change the builder's state.
     */
    @Test
    public void apply_withEmptyPhonemeRule_shouldNotAlterEmptyBuilder() {
        // Arrange
        // 1. Define a language set required for creating phonemes and builders.
        Set<String> languageNames = new LinkedHashSet<>(Collections.singleton("common"));
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languageNames);

        // 2. Create an empty PhonemeBuilder. Per documentation, it starts with a single, empty phoneme.
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);

        // 3. Create a rule that represents an empty phoneme.
        CharSequence emptyPhonemeText = CharBuffer.allocate(0);
        Rule.Phoneme phonemeRuleToApply = new Rule.Phoneme(emptyPhonemeText, languageSet);
        
        final int maxPhonemes = 32;

        // Act
        // Apply the empty phoneme rule to the builder.
        builder.apply(phonemeRuleToApply, maxPhonemes);

        // Assert
        // The builder's state should be unchanged: it should still contain one single, empty phoneme.
        Set<Rule.Phoneme> resultingPhonemes = builder.getPhonemes();
        assertEquals("The builder should still contain exactly one phoneme.", 1, resultingPhonemes.size());

        // The string representation of a builder with a single empty phoneme is an empty string.
        assertEquals("The resulting phoneme's text should be empty.", "", builder.makeString());
    }
}