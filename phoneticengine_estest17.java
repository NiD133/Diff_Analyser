package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Set;

/**
 * Tests for {@link PhoneticEngine.PhonemeBuilder}.
 */
public class PhoneticEngine_PhonemeBuilderTest {

    /**
     * Tests that applying a phoneme expression to an empty PhonemeBuilder
     * results in a builder containing that single phoneme.
     */
    @Test
    public void applyShouldAppendPhonemeToEmptyBuilder() {
        // Arrange
        // 1. Define a language set. The PhonemeBuilder requires one.
        Set<String> languageNames = Collections.singleton("any");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languageNames);

        // 2. Create a PhonemeBuilder that starts with a single empty phoneme.
        PhoneticEngine.PhonemeBuilder phonemeBuilder = PhoneticEngine.PhonemeBuilder.empty(languageSet);

        // 3. Create the phoneme expression to be applied.
        String phonemeText = "test-phoneme";
        Rule.Phoneme phonemeToApply = new Rule.Phoneme(phonemeText, languageSet);
        
        // 4. Define the maximum number of phonemes to generate.
        final int maxPhonemes = 10;

        // Act
        // Apply the phoneme expression. This should append the new phoneme text
        // to the builder's initial empty phoneme.
        phonemeBuilder.apply(phonemeToApply, maxPhonemes);

        // Assert
        // The builder should now contain the phoneme that was applied.
        String expectedPhonemes = phonemeText;
        assertEquals("The builder should contain the applied phoneme", expectedPhonemes, phonemeBuilder.makeString());
    }
}