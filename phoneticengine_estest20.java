package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link PhoneticEngine.PhonemeBuilder} inner class.
 */
public class PhoneticEngine_PhonemeBuilderTest {

    /**
     * This test verifies that the append operation on a PhonemeBuilder
     * does not have side effects on the original data structures used
     * to create it.
     */
    @Test
    public void appendShouldNotModifyOriginalLanguageSet() {
        // Arrange: Create an empty set of language names. This set will be used to
        // initialize a LanguageSet and, subsequently, a PhonemeBuilder.
        // The test's goal is to ensure this original set remains unchanged.
        Set<String> languageNames = new HashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languageNames);
        PhoneticEngine.PhonemeBuilder phonemeBuilder = PhoneticEngine.PhonemeBuilder.empty(languageSet);

        // Act: Perform an append operation on the builder.
        phonemeBuilder.append("");

        // Assert: The original set of language names should not have been modified
        // by the append operation. It should still be empty.
        assertTrue("The original language name set should remain empty after the append operation.",
                   languageNames.isEmpty());
    }
}