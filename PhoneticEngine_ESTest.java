package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.apache.commons.codec.language.bm.Rule;
import org.apache.commons.codec.language.bm.RuleType;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class PhoneticEngine_ESTest extends PhoneticEngine_ESTest_scaffolding {

    // ========================================================================
    // Constructor Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testConstructor_ConcatFalse_SetsCorrectProperties() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.ASHKENAZI, 
            RuleType.APPROX, 
            false
        );
        
        assertFalse(engine.isConcat());
        assertEquals(20, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testConstructor_WithMaxPhonemesParameter_SetsCorrectValue() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            false, 
            1
        );
        
        assertFalse(engine.isConcat());
        assertEquals(1, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNegativeMaxPhonemes_Allowed() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.ASHKENAZI, 
            RuleType.EXACT, 
            true, 
            -516
        );
        
        assertTrue(engine.isConcat());
        assertEquals(-516, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructor_RuleTypeRules_ThrowsException() {
        // RuleType.RULES is not allowed in constructor
        new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.RULES, 
            true, 
            772
        );
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructor_NegativeMaxPhonemes_ThrowsWhenUsed() {
        // Constructor with negative maxPhonemes should throw when used
        PhoneticEngine engine = new PhoneticEngine(
            NameType.ASHKENAZI, 
            RuleType.APPROX, 
            false, 
            -1141
        );
        
        engine.encode("c)mY)");
    }

    // ========================================================================
    // Getter Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testIsConcat_WhenTrue_ReturnsCorrectValue() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.APPROX, 
            true, 
            0
        );
        
        assertTrue(engine.isConcat());
        assertEquals(0, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testGetRuleType_ReturnsCorrectValue() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.APPROX, 
            false
        );
        
        assertEquals(RuleType.APPROX, engine.getRuleType());
        assertFalse(engine.isConcat());
        assertEquals(20, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testGetLang_ReturnsInstance() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.EXACT, 
            true
        );
        
        assertNotNull(engine.getLang());
        assertTrue(engine.isConcat());
        assertEquals(20, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testGetNameType_ReturnsCorrectValue() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            true
        );
        
        assertEquals(NameType.GENERIC, engine.getNameType());
        assertTrue(engine.isConcat());
        assertEquals(20, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testGetMaxPhonemes_WhenSetToZero_ReturnsZero() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.ASHKENAZI, 
            RuleType.EXACT, 
            false, 
            0
        );
        
        assertFalse(engine.isConcat());
        assertEquals(0, engine.getMaxPhonemes());
    }

    // ========================================================================
    // encode() Method Tests
    // ========================================================================
    
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testEncode_NullLanguageSet_ThrowsNPE() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            false
        );
        
        engine.encode("8C?#]", null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testEncode_InvalidLanguageSet_ThrowsIllegalArgumentException() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            false
        );
        
        LinkedHashSet<String> invalidLanguages = new LinkedHashSet<>();
        invalidLanguages.add("de la daorg.apache.commons.codec.language.bm.languages$languageset");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(invalidLanguages);
        
        engine.encode("de la daorg.apache.commons.codec.language.bm.languages$languageset", languageSet);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testEncode_NullInput_ThrowsNPE() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.EXACT, 
            true
        );
        
        engine.encode(null);
    }

    @Test(timeout = 4000)
    public void testEncode_WithEmptyLanguageSet_ProcessesCorrectly() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.EXACT, 
            false
        );
        
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        // Very long input string
        String longInput = "langvadZesoibdZgZdf|langvadZesoibdZgxdf|langvadZesojbdZgZdf|"
            + "langvadZesojbdZgxdf|langvagesoibdZgZdf|langvagesoibdZgxdf|langvagesojbdZgZdf|"
            + "langvagesojbdZgxdf|langvaxesoibdZgZdf|langvaxesoibdZgxdf|langvaxesojbdZgZdf|"
            + "langvaxesojbdZgxdf-rf|rp";
        
        engine.encode(longInput, languageSet);
    }

    @Test(timeout = 4000)
    public void testEncode_Ben_WithEmptyLanguageSet_ReturnsEmptyString() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.ASHKENAZI, 
            RuleType.EXACT, 
            false, 
            0
        );
        
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        engine.encode("ben", languageSet);
        assertFalse(engine.isConcat());
        assertEquals(0, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testEncode_ApostropheInInput_ReturnsExpectedEncoding() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            true
        );
        
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        String result = engine.encode("d'", languageSet);
        assertEquals("()-(t)", result);
        assertTrue(engine.isConcat());
        assertEquals(20, engine.getMaxPhonemes());
    }

    @Test(timeout = 4000)
    public void testEncode_SpecialCharacters_ReturnsEmptyString() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            false
        );
        
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        String result = engine.encode("X&K[T6mL;i'", languageSet);
        assertEquals("", result);
        assertFalse(engine.isConcat());
    }

    @Test(timeout = 4000)
    public void testEncode_DellaLanguages_ReturnsExpectedEncoding() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.APPROX, 
            true
        );
        engine.getMaxPhonemes(); // Force max phonemes to default (20)
        
        String result = engine.encode("della languages([o/,y)ptjgjdw~` r7b])");
        assertTrue(result.startsWith("langvagis"));
        assertTrue(result.contains("|"));
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testEncode_WithNegativeMaxPhonemes_ThrowsWhenUsed() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.APPROX, 
            true, 
            -1
        );
        
        engine.encode("des phonme expression contains a '[' but does not end in ']'", languageSet);
    }

    @Test(timeout = 4000)
    public void testEncode_DuDella_ReturnsExpectedValue() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.SEPHARDIC, 
            RuleType.EXACT, 
            false
        );
        
        String result = engine.encode("du della");
        assertEquals("dudela", result);
    }

    @Test(timeout = 4000)
    public void testEncode_MalformedImport_ReturnsExpectedEncoding() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.ASHKENAZI, 
            RuleType.EXACT, 
            false
        );
        
        String result = engine.encode("Malformed import statement '");
        assertEquals(20, engine.getMaxPhonemes());
        assertTrue(result.contains("malformet"));
        assertTrue(result.contains("import"));
    }

    @Test(timeout = 4000)
    public void testEncode_ComplexInput_WithMaxPhonemesZero_ReturnsEmpty() {
        PhoneticEngine engine = new PhoneticEngine(
            NameType.GENERIC, 
            RuleType.EXACT, 
            true, 
            0
        );
        
        String result = engine.encode("7l\"UR`wP/oT{zhy_[");
        assertEquals("", result);
        assertEquals(0, engine.getMaxPhonemes());
        assertTrue(engine.isConcat());
    }

    // ========================================================================
    // PhonemeBuilder Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testPhonemeBuilder_ApplyPhoneme_SizeUpdatesCorrectly() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        languages.add("common");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        CharBuffer buffer = CharBuffer.allocate(0);
        Rule.Phoneme phoneme = new Rule.Phoneme(buffer, languageSet);
        
        builder.apply(phoneme, 32);
        assertEquals(1, phoneme.size());
    }

    @Test(timeout = 4000)
    public void testPhonemeBuilder_ApplyPhonemeWithZeroLength_SizeUpdates() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        languages.add("common");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        CharBuffer buffer = CharBuffer.allocate(0);
        Rule.Phoneme phoneme = new Rule.Phoneme(buffer, languageSet);
        
        builder.apply(phoneme, 0);
        assertEquals(1, phoneme.size());
    }

    @Test(timeout = 4000)
    public void testPhonemeBuilder_ApplyPhonemeWithContent_SizeUpdates() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        languages.add(">7=;2[R[bkK,");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        Rule.Phoneme phoneme = new Rule.Phoneme("O/,y)ptjgJDw~` r7b", languageSet);
        
        builder.apply(phoneme, 1);
        assertEquals(1, phoneme.size());
    }

    @Test(timeout = 4000)
    public void testPhonemeBuilder_ApplyEmptyPhoneme_SizeUpdates() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        Rule.Phoneme phoneme = new Rule.Phoneme("", languageSet);
        
        builder.apply(phoneme, 600);
        assertEquals(1, phoneme.size());
    }

    @Test(timeout = 4000)
    public void testPhonemeBuilder_MakeString_ReturnsEmptyForEmptyBuilder() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        String result = builder.makeString();
        assertFalse(languages.contains(result));
    }

    @Test(timeout = 4000)
    public void testPhonemeBuilder_AppendEmptyString_NoChange() {
        LinkedHashSet<String> languages = new LinkedHashSet<>(20);
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        builder.append("");
        assertFalse(languages.contains(""));
    }

    @Test(timeout = 4000)
    public void testPhonemeBuilder_GetPhonemes_ReturnsCorrectSize() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        Set<Rule.Phoneme> phonemes = builder.getPhonemes();
        assertEquals(1, phonemes.size());
    }
}