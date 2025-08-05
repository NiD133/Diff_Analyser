package org.apache.commons.codec.language.bm;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the {@link PhoneticEngine}.
 * The original test suite was auto-generated and has been refactored for clarity and maintainability.
 * The EvoSuite runner annotations are kept to ensure compatibility with the original test environment.
 */
@RunWith(Enclosed.class)
public class PhoneticEngineTest {

    // Note: The PhoneticEngine_ESTest_scaffolding superclass and EvoSuite runner parameters
    // are part of the original auto-generated test setup. They are preserved here to
    // maintain the original test execution context.
    public static class EngineBehaviorTest extends PhoneticEngine_ESTest_scaffolding {

        // --- Constructor Tests ---

        /**
         * Tests that the 3-argument constructor correctly sets the engine's properties
         * and initializes maxPhonemes to its default value.
         */
        @Test
        public void constructorShouldSetPropertiesAndUseDefaultMaxPhonemes() {
            // Arrange
            NameType nameType = NameType.ASHKENAZI;
            RuleType ruleType = RuleType.APPROX;
            boolean concat = false;

            // Act
            PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat);

            // Assert
            assertEquals(nameType, engine.getNameType());
            assertEquals(ruleType, engine.getRuleType());
            assertEquals(concat, engine.isConcat());
            assertEquals("Default maxPhonemes should be 20", 20, engine.getMaxPhonemes());
        }

        /**
         * Tests that the 4-argument constructor correctly sets all engine properties,
         * including a custom value for maxPhonemes.
         */
        @Test
        public void constructorWithMaxPhonemesShouldSetPropertiesCorrectly() {
            // Arrange
            NameType nameType = NameType.GENERIC;
            RuleType ruleType = RuleType.EXACT;
            boolean concat = true;
            int maxPhonemes = 5;

            // Act
            PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat, maxPhonemes);

            // Assert
            assertEquals(nameType, engine.getNameType());
            assertEquals(ruleType, engine.getRuleType());
            assertEquals(concat, engine.isConcat());
            assertEquals(maxPhonemes, engine.getMaxPhonemes());
        }

        /**
         * Tests that the PhoneticEngine constructor throws an IllegalArgumentException
         * when initialized with the unsupported RuleType.RULES.
         */
        @Test(expected = IllegalArgumentException.class)
        public void constructorShouldThrowExceptionForUnsupportedRulesType() {
            // The RuleType.RULES is not a valid type for engine construction.
            new PhoneticEngine(NameType.GENERIC, RuleType.RULES, true);
        }

        // --- Getter Tests ---

        @Test
        public void gettersShouldReturnConfiguredValues() {
            // Arrange
            NameType nameType = NameType.SEPHARDIC;
            RuleType ruleType = RuleType.EXACT;
            PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, true, 10);

            // Act & Assert
            assertTrue(engine.isConcat());
            assertEquals(10, engine.getMaxPhonemes());
            assertEquals(nameType, engine.getNameType());
            assertEquals(ruleType, engine.getRuleType());
            assertNotNull(engine.getLang());
        }

        // --- encode(String) Tests ---

        @Test(expected = NullPointerException.class)
        public void encodeStringShouldThrowExceptionForNullInput() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, true);

            // Act
            engine.encode(null);
        }

        @Test
        public void encodeShouldHandleNamePrefixesAndSpaces() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, false);

            // Act
            String result = engine.encode("du della");

            // Assert
            // The engine should recognize "du" and "della" as name prefixes, strip them,
            // and encode the remaining parts, removing the space.
            assertEquals("dudela", result);
        }

        @Test
        public void encodeMultiWordStringShouldEncodeEachWord() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.EXACT, false);

            // Act
            String result = engine.encode("Malformed import statement");

            // Assert
            // When concat is false, each word is encoded separately. The pipe '|' separates
            // alternative phonemes for a single word.
            assertEquals("malformet-import-Statement|statement-", result);
        }

        @Test(expected = IllegalArgumentException.class)
        public void encodeShouldThrowExceptionIfEngineCreatedWithNegativeMaxPhonemes() {
            // Arrange
            // The constructor allows a negative maxPhonemes, but this causes an internal
            // HashMap to be created with a negative capacity during encoding.
            PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.APPROX, false, -100);

            // Act
            engine.encode("test");
        }

        @Test
        public void encodeShouldReturnEmptyStringWhenMaxPhonemesIsZero() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, true, 0);

            // Act
            String result = engine.encode("any string");

            // Assert
            // If maxPhonemes is 0, no phonemes should be generated.
            assertEquals("", result);
        }

        // --- encode(String, LanguageSet) Tests ---

        @Test(expected = NullPointerException.class)
        public void encodeWithLanguageSetShouldThrowExceptionForNullLanguageSet() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, false);

            // Act
            engine.encode("test", null);
        }

        @Test
        public void encodeShouldThrowExceptionWhenNoRulesFoundForLanguage() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);
            Set<String> languages = new LinkedHashSet<>();
            // This language name does not correspond to any known rule set.
            String nonExistentLanguage = "not-a-real-language";
            languages.add(nonExistentLanguage);
            Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);

            // Act & Assert
            try {
                engine.encode("test", languageSet);
                fail("Expected an IllegalArgumentException because no rules exist for the given language.");
            } catch (IllegalArgumentException e) {
                // The exception message confirms that rule lookup failed.
                assertTrue(e.getMessage().startsWith("No rules found for"));
            }
        }

        @Test
        public void encodeWithApostropheShouldBeTreatedAsSeparator() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, true);
            Languages.LanguageSet languageSet = Languages.LanguageSet.from(Collections.emptySet());

            // Act
            String result = engine.encode("d'", languageSet);

            // Assert
            // The apostrophe acts as a word separator, resulting in two encoded parts.
            assertEquals("()-(t)", result);
        }

        @Test
        public void encodeWithNonAlphabeticCharsShouldReturnEmptyString() {
            // Arrange
            PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);
            Languages.LanguageSet languageSet = Languages.LanguageSet.from(Collections.emptySet());

            // Act
            String result = engine.encode("123-!@#-$%^", languageSet);

            // Assert
            // The engine should ignore non-alphabetic characters, producing an empty result.
            assertEquals("", result);
        }
    }

    /**
     * Tests for the static inner class {@link PhoneticEngine.PhonemeBuilder}.
     */
    public static class PhonemeBuilderTest extends PhoneticEngine_ESTest_scaffolding {

        /**
         * Tests that an "empty" PhonemeBuilder is initialized with a single, empty phoneme,
         * as per its contract.
         */
        @Test
        public void emptyBuilderShouldContainOneInitialPhoneme() {
            // Arrange
            Languages.LanguageSet languageSet = Languages.LanguageSet.from(Collections.emptySet());

            // Act
            PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
            Set<Rule.Phoneme> phonemes = builder.getPhonemes();

            // Assert
            assertEquals("An empty builder should start with one phoneme.", 1, phonemes.size());
            assertEquals("The initial phoneme should be empty.", "", phonemes.iterator().next().getPhonemeText().toString());
        }

        /**
         * Tests that calling makeString on a newly created builder returns an empty string.
         */
        @Test
        public void makeStringOnEmptyBuilderShouldReturnEmptyString() {
            // Arrange
            Languages.LanguageSet languageSet = Languages.LanguageSet.from(Collections.emptySet());
            PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);

            // Act
            String result = builder.makeString();

            // Assert
            assertEquals("", result);
        }

        /**
         * Tests that the append method correctly adds text to the phonemes in the builder.
         */
        @Test
        public void appendShouldAddTextToAllPhonemes() {
            // Arrange
            Languages.LanguageSet languageSet = Languages.LanguageSet.from(Collections.emptySet());
            PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);

            // Act
            builder.append("test");
            String result = builder.makeString();

            // Assert
            assertEquals("test", result);
        }
    }
}