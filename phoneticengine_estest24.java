package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PhoneticEngine} class.
 */
public class PhoneticEngineTest {

    /**
     * Tests that the PhoneticEngine constructor correctly sets the default maximum number of phonemes.
     */
    @Test
    public void constructor_shouldSetDefaultMaxPhonemes() {
        // Arrange
        final PhoneticEngine phoneticEngine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.APPROX, true);
        final int expectedMaxPhonemes = 20;

        // Act
        final int actualMaxPhonemes = phoneticEngine.getMaxPhonemes();

        // Assert
        assertEquals(expectedMaxPhonemes, actualMaxPhonemes);
    }

    /**
     * Tests the encode method with a complex input string that includes a name prefix,
     * multiple words, and special characters. It verifies that the engine produces the
     * correct set of phonetic representations.
     */
    @Test
    public void encode_withSephardicNameTypeAndComplexInput_shouldProduceCorrectPhonemes() {
        // Arrange
        final NameType nameType = NameType.SEPHARDIC;
        final RuleType ruleType = RuleType.APPROX;
        final boolean concatenate = true;
        final PhoneticEngine phoneticEngine = new PhoneticEngine(nameType, ruleType, concatenate);

        // This input tests several features:
        // - "della": A Sephardic name prefix that should be stripped before encoding.
        // - "languages...": The core text to be encoded, including non-alphabetic characters
        //   that the engine must handle.
        final String complexInput = "della languages([o/,y)ptjgjdw~` r7b])";

        // The expected result is a set of possible phonetic encodings.
        // Using a Set for comparison makes the test robust against changes in the order of results.
        final Set<String> expectedPhonemes = new HashSet<>(Arrays.asList(
            "langvagisDbzghdvrf", "langvagisDbzghdvrp", "langvagisDbzgzdvrf", "langvagisDbzgzdvrp",
            "langvagisuibzghdvrf", "langvagisuibzghdvrp", "langvagisuibzgzdvrf", "langvagisuibzgzdvrp",
            "langvahisDbzghdvrf", "langvahisDbzghdvrp", "langvahisDbzgzdvrf", "langvahisDbzgzdvrp",
            "langvahisuibzghdvrf", "langvahisuibzghdvrp", "langvahisuibzgzdvrf", "langvahisuibzgzdvrp",
            "langvazisuibzghdvrf", "langvazisuibzghdvrp", "langvazisuibzgzdvrf", "langvazisuibzgzdvrp"
        ));

        // Act
        final String encodedOutput = phoneticEngine.encode(complexInput);
        final Set<String> actualPhonemes = new HashSet<>(Arrays.asList(encodedOutput.split("\\|")));

        // Assert
        assertEquals("The set of generated phonemes should match the expected set.",
                expectedPhonemes, actualPhonemes);
    }
}