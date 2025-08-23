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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class PhoneticEngine_ESTest extends PhoneticEngine_ESTest_scaffolding {

    private static final int DEFAULT_TIMEOUT = 4000;
    private static final int DEFAULT_MAX_PHONEMES = 20;

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhoneticEngineWithAshkenaziAndApprox() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.APPROX, false);
        assertFalse(engine.isConcat());
        assertEquals(DEFAULT_MAX_PHONEMES, engine.getMaxPhonemes());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhoneticEngineWithGenericAndExact() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false, 1);
        assertFalse(engine.isConcat());
        assertEquals(1, engine.getMaxPhonemes());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhoneticEngineWithNegativeMaxPhonemes() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.EXACT, true, -516);
        assertTrue(engine.isConcat());
        assertEquals(-516, engine.getMaxPhonemes());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithNullLanguageSetThrowsException() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);
        try {
            engine.encode("8C?#]", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.bm.Rule", e);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithInvalidLanguageSetThrowsException() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        languages.add("de la daorg.apache.commons.codec.language.bm.languages$languageset");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        try {
            engine.encode("de la daorg.apache.commons.codec.language.bm.languages$languageset", languageSet);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.bm.Rule", e);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithNullStringThrowsException() {
        PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, true);
        try {
            engine.encode(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.bm.Lang", e);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithNegativeInitialCapacityThrowsException() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.APPROX, false, -1141);
        try {
            engine.encode("c)mY)");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.HashMap", e);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhoneticEngineWithInvalidRuleTypeThrowsException() {
        try {
            new PhoneticEngine(NameType.SEPHARDIC, RuleType.RULES, true, 772);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.bm.PhoneticEngine", e);
        }
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithEmptyLanguageSet() {
        PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, false);
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        engine.encode("langvadZesoibdZgZdf|langvadZesoibdZgxdf|langvadZesojbdZgZdf|langvadZesojbdZgxdf|langvagesoibdZgZdf|langvagesoibdZgxdf|langvagesojbdZgZdf|langvagesojbdZgxdf|langvaxesoibdZgZdf|langvaxesoibdZgxdf|langvaxesojbdZgZdf|langvaxesojbdZgxdf-rf|rp", languageSet);
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithZeroMaxPhonemes() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.EXACT, false, 0);
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        engine.encode("ben", languageSet);
        assertFalse(engine.isConcat());
        assertEquals(0, engine.getMaxPhonemes());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithEmptyLanguageSetReturnsEmptyString() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        String result = engine.encode("X&K[T6mL;i'", languageSet);
        assertEquals("", result);
        assertFalse(engine.isConcat());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testEncodeWithPhonemeBuilder() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        languages.add("common");
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        PhoneticEngine.PhonemeBuilder phonemeBuilder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        CharBuffer charBuffer = CharBuffer.allocate(0);
        Rule.Phoneme phoneme = new Rule.Phoneme(charBuffer, languageSet);
        phonemeBuilder.apply(phoneme, 32);
        assertEquals(1, phoneme.size());
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhonemeBuilderEmpty() {
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        Languages.LanguageSet languageSet = Languages.LanguageSet.from(languages);
        PhoneticEngine.PhonemeBuilder phonemeBuilder = PhoneticEngine.PhonemeBuilder.empty(languageSet);
        String result = phonemeBuilder.makeString();
        assertFalse(languages.contains(result));
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhoneticEngineEncodeWithValidInput() {
        PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, false);
        String result = engine.encode("du della");
        assertEquals("dudela", result);
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void testPhoneticEngineEncodeWithMalformedInput() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.EXACT, false);
        String result = engine.encode("Malformed import statement '");
        assertEquals("malformet-import-Statement|statement-", result);
    }
}