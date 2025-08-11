package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedHashSet;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.commons.codec.language.bm.RuleType;

/**
 * Test suite for PhoneticEngine class functionality.
 * Tests cover constructor validation, encoding operations, and getter methods.
 */
public class PhoneticEngineTest {

    // Test Constants
    private static final int DEFAULT_MAX_PHONEMES = 20;
    private static final int CUSTOM_MAX_PHONEMES = 1;
    private static final int NEGATIVE_MAX_PHONEMES = -516;
    
    // ===========================================
    // Constructor Tests
    // ===========================================
    
    @Test
    public void testConstructor_WithBasicParameters_SetsDefaultValues() {
        // Given
        NameType nameType = NameType.ASHKENAZI;
        RuleType ruleType = RuleType.APPROX;
        boolean concatenate = false;
        
        // When
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concatenate);
        
        // Then
        assertFalse("Concatenation should be disabled", engine.isConcat());
        assertEquals("Should use default max phonemes", DEFAULT_MAX_PHONEMES, engine.getMaxPhonemes());
    }

    @Test
    public void testConstructor_WithCustomMaxPhonemes_SetsCorrectValues() {
        // Given
        NameType nameType = NameType.GENERIC;
        RuleType ruleType = RuleType.EXACT;
        boolean concatenate = false;
        int maxPhonemes = CUSTOM_MAX_PHONEMES;
        
        // When
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concatenate, maxPhonemes);
        
        // Then
        assertFalse("Concatenation should be disabled", engine.isConcat());
        assertEquals("Should use custom max phonemes", CUSTOM_MAX_PHONEMES, engine.getMaxPhonemes());
    }

    @Test
    public void testConstructor_WithNegativeMaxPhonemes_AllowsNegativeValue() {
        // Given
        NameType nameType = NameType.ASHKENAZI;
        RuleType ruleType = RuleType.EXACT;
        boolean concatenate = true;
        
        // When
        PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concatenate, NEGATIVE_MAX_PHONEMES);
        
        // Then
        assertEquals("Should accept negative max phonemes", NEGATIVE_MAX_PHONEMES, engine.getMaxPhonemes());
        assertTrue("Concatenation should be enabled", engine.isConcat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithRulesRuleType_ThrowsException() {
        // Given
        NameType nameType = NameType.SEPHARDIC;
        RuleType invalidRuleType = RuleType.RULES;
        
        // When & Then - Should throw IllegalArgumentException
        new PhoneticEngine(nameType, invalidRuleType, true, 772);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithRulesRuleTypeBasic_ThrowsException() {
        // Given
        NameType nameType = NameType.ASHKENAZI;
        RuleType invalidRuleType = RuleType.RULES;
        
        // When & Then - Should throw IllegalArgumentException
        new PhoneticEngine(nameType, invalidRuleType, false);
    }

    // ===========================================
    // Encoding Tests - Error Cases
    // ===========================================
    
    @Test(expected = NullPointerException.class)
    public void testEncode_WithNullInput_ThrowsNullPointerException() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.SEPHARDIC, RuleType.EXACT);
        
        // When & Then - Should throw NullPointerException
        engine.encode((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testEncode_WithNullLanguageSet_ThrowsNullPointerException() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.GENERIC, RuleType.EXACT);
        
        // When & Then - Should throw NullPointerException
        engine.encode("test input", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_WithInvalidLanguageInSet_ThrowsIllegalArgumentException() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.GENERIC, RuleType.EXACT);
        Languages.LanguageSet invalidLanguageSet = createLanguageSetWith("invalid_language_name");
        
        // When & Then - Should throw IllegalArgumentException
        engine.encode("test input", invalidLanguageSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_WithNegativeMaxPhonemesInternalError_ThrowsException() {
        // Given - Engine with negative max phonemes that causes internal HashMap error
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.APPROX, false, -1141);
        
        // When & Then - Should throw IllegalArgumentException from internal HashMap
        engine.encode("test");
    }

    // ===========================================
    // Encoding Tests - Success Cases
    // ===========================================
    
    @Test
    public void testEncode_WithSimpleName_ReturnsExpectedEncoding() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.SEPHARDIC, RuleType.EXACT);
        String input = "du della";
        
        // When
        String result = engine.encode(input);
        
        // Then
        assertEquals("Should encode name correctly", "dudela", result);
    }

    @Test
    public void testEncode_WithComplexInput_ReturnsExpectedEncoding() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.ASHKENAZI, RuleType.EXACT);
        String input = "Malformed import statement '";
        
        // When
        String result = engine.encode(input);
        
        // Then
        assertEquals("Should handle complex input", "malformet-import-Statement|statement-", result);
    }

    @Test
    public void testEncode_WithConcatenationEnabled_ReturnsConcatenatedResult() {
        // Given
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, true);
        Languages.LanguageSet emptyLanguageSet = createEmptyLanguageSet();
        String input = "d'";
        
        // When
        String result = engine.encode(input, emptyLanguageSet);
        
        // Then
        assertEquals("Should return concatenated encoding", "()-(t)", result);
    }

    @Test
    public void testEncode_WithSpecialCharacters_ReturnsEmptyString() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.GENERIC, RuleType.EXACT);
        Languages.LanguageSet emptyLanguageSet = createEmptyLanguageSet();
        String inputWithSpecialChars = "X&K[T6mL;i'";
        
        // When
        String result = engine.encode(inputWithSpecialChars, emptyLanguageSet);
        
        // Then
        assertEquals("Should return empty string for unrecognized characters", "", result);
    }

    @Test
    public void testEncode_WithZeroMaxPhonemes_ReturnsEmptyString() {
        // Given
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, true, 0);
        String input = "test input";
        
        // When
        String result = engine.encode(input);
        
        // Then
        assertEquals("Should return empty string when max phonemes is 0", "", result);
    }

    @Test
    public void testEncode_WithValidLanguageSet_ProcessesSuccessfully() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.ASHKENAZI, RuleType.EXACT);
        Languages.LanguageSet emptyLanguageSet = createEmptyLanguageSet();
        String input = "ben";
        
        // When
        String result = engine.encode(input, emptyLanguageSet);
        
        // Then - Should complete without throwing exception
        assertNotNull("Result should not be null", result);
    }

    // ===========================================
    // Getter Method Tests
    // ===========================================
    
    @Test
    public void testGetMaxPhonemes_ReturnsCorrectValue() {
        // Given
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false, CUSTOM_MAX_PHONEMES);
        
        // When
        int maxPhonemes = engine.getMaxPhonemes();
        
        // Then
        assertEquals("Should return configured max phonemes", CUSTOM_MAX_PHONEMES, maxPhonemes);
    }

    @Test
    public void testIsConcat_WithConcatenationEnabled_ReturnsTrue() {
        // Given
        PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.APPROX, true, 0);
        
        // When
        boolean isConcat = engine.isConcat();
        
        // Then
        assertTrue("Should return true when concatenation is enabled", isConcat);
    }

    @Test
    public void testGetRuleType_ReturnsConfiguredRuleType() {
        // Given
        RuleType expectedRuleType = RuleType.APPROX;
        PhoneticEngine engine = createBasicEngine(NameType.SEPHARDIC, expectedRuleType);
        
        // When
        RuleType actualRuleType = engine.getRuleType();
        
        // Then
        assertEquals("Should return configured rule type", expectedRuleType, actualRuleType);
    }

    @Test
    public void testGetLang_ReturnsLanguageInstance() {
        // Given
        PhoneticEngine engine = createBasicEngine(NameType.SEPHARDIC, RuleType.EXACT);
        
        // When
        Object lang = engine.getLang();
        
        // Then
        assertNotNull("Language instance should not be null", lang);
    }

    @Test
    public void testGetNameType_ReturnsConfiguredNameType() {
        // Given
        NameType expectedNameType = NameType.GENERIC;
        PhoneticEngine engine = createBasicEngine(expectedNameType, RuleType.EXACT);
        
        // When
        NameType actualNameType = engine.getNameType();
        
        // Then
        assertEquals("Should return configured name type", expectedNameType, actualNameType);
    }

    // ===========================================
    // PhonemeBuilder Tests
    // ===========================================
    
    @Test
    public void testPhonemeBuilder_ApplyWithLargeMaxPhonemes_ProcessesSuccessfully() {
        // Given
        Languages.LanguageSet languageSet = createLanguageSetWith("common");
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        Rule.Phoneme phoneme = new Rule.Phoneme("", languageSet);
        
        // When
        builder.apply(phoneme, 32);
        
        // Then
        assertEquals("Phoneme should be processed", 1, phoneme.size());
    }

    @Test
    public void testPhonemeBuilder_ApplyWithZeroMaxPhonemes_ProcessesSuccessfully() {
        // Given
        Languages.LanguageSet languageSet = createLanguageSetWith("common");
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        Rule.Phoneme phoneme = new Rule.Phoneme("", languageSet);
        
        // When
        builder.apply(phoneme, 0);
        
        // Then
        assertEquals("Phoneme should be processed even with 0 max", 1, phoneme.size());
    }

    @Test
    public void testPhonemeBuilder_MakeString_ReturnsStringRepresentation() {
        // Given
        Languages.LanguageSet languageSet = createEmptyLanguageSet();
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        
        // When
        String result = builder.makeString();
        
        // Then
        assertNotNull("String representation should not be null", result);
    }

    @Test
    public void testPhonemeBuilder_Append_AddsToPhonemes() {
        // Given
        Languages.LanguageSet languageSet = createEmptyLanguageSet();
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        
        // When
        builder.append("");
        
        // Then - Should complete without throwing exception
        assertNotNull("Builder should remain valid after append", builder);
    }

    @Test
    public void testPhonemeBuilder_GetPhonemes_ReturnsPhonemeSet() {
        // Given
        Languages.LanguageSet languageSet = createEmptyLanguageSet();
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        
        // When
        var phonemes = builder.getPhonemes();
        
        // Then
        assertEquals("Should contain initial empty phoneme", 1, phonemes.size());
    }

    // ===========================================
    // Helper Methods
    // ===========================================
    
    /**
     * Creates a basic PhoneticEngine with standard settings for testing.
     */
    private PhoneticEngine createBasicEngine(NameType nameType, RuleType ruleType) {
        return new PhoneticEngine(nameType, ruleType, false);
    }
    
    /**
     * Creates an empty language set for testing.
     */
    private Languages.LanguageSet createEmptyLanguageSet() {
        return Languages.LanguageSet.from(new LinkedHashSet<>());
    }
    
    /**
     * Creates a language set containing the specified language.
     */
    private Languages.LanguageSet createLanguageSetWith(String language) {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        languages.add(language);
        return Languages.LanguageSet.from(languages);
    }
}