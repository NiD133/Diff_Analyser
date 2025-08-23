package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

/**
 * Tests for the {@link PhoneticEngine.PhonemeBuilder} inner class.
 */
public class PhoneticEngine_PhonemeBuilderTest {

    /**
     * Tests that calling makeString() on a PhonemeBuilder created via the static
     * empty() factory method results in an empty string.
     *
     * According to its documentation, PhonemeBuilder.empty() creates a builder
     * containing "a single phoneme of zero characters". This test verifies that
     * serializing this empty phoneme correctly produces an empty string.
     */
    @Test
    public void makeStringOnEmptyBuilderShouldReturnEmptyString() {
        // Arrange: Create an empty PhonemeBuilder.
        // This requires an empty LanguageSet.
        Languages.LanguageSet emptyLanguageSet = Languages.LanguageSet.from(Collections.emptySet());
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(emptyLanguageSet);

        // Act: Generate the string representation from the builder.
        String phonemeString = builder.makeString();

        // Assert: The resulting string should be empty.
        assertEquals("The string from an empty PhonemeBuilder should be empty", "", phonemeString);
    }
}