package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link PhoneticEngine.PhonemeBuilder} class.
 */
public class PhoneticEnginePhonemeBuilderTest {

    @Test
    public void emptyBuilderShouldContainOneInitialPhoneme() {
        // Arrange: Create an empty language set, which is a required parameter.
        Languages.LanguageSet emptyLanguageSet = Languages.LanguageSet.from(Collections.emptySet());

        // Act: Create an "empty" PhonemeBuilder.
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(emptyLanguageSet);
        Set<Rule.Phoneme> phonemes = builder.getPhonemes();

        // Assert: The builder should contain a single, base phoneme.
        // This initial phoneme acts as a starting point for appending other phonemes.
        assertEquals("An 'empty' builder should start with one base phoneme.", 1, phonemes.size());
    }
}