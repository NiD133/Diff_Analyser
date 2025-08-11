package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneticEngineTest {

    // Constructor and basic configuration

    @Test
    public void defaultConfigurationHasExpectedValues() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.APPROX, false);

        assertFalse(engine.isConcat());
        assertEquals(20, engine.getMaxPhonemes());
        assertEquals(NameType.ASHKENAZI, engine.getNameType());
        assertEquals(RuleType.APPROX, engine.getRuleType());
        assertNotNull(engine.getLang());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsRuleTypeRULES() {
        new PhoneticEngine(NameType.GENERIC, RuleType.RULES, true);
    }

    @Test
    public void explicitMaxPhonemesIsRespected() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false, 1);

        assertEquals(1, engine.getMaxPhonemes());
        assertFalse(engine.isConcat());
    }

    @Test
    public void concatFlagIsReflected() {
        PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.APPROX, true, 0);

        assertTrue(engine.isConcat());
        assertEquals(0, engine.getMaxPhonemes());
    }

    // Encoding API: happy paths

    @Test
    public void encodeReturnsNonNullForSimpleInput() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.EXACT, false);

        String encoded = engine.encode("ben");

        assertNotNull(encoded);
    }

    @Test
    public void encodeWithExplicitLanguageSetRuns() {
        PhoneticEngine engine = new PhoneticEngine(NameType.ASHKENAZI, RuleType.EXACT, false);

        String encoded = engine.encode("ben", Languages.ANY_LANGUAGE);

        assertNotNull(encoded);
    }

    @Test
    public void zeroMaxPhonemesProducesEmptyEncoding() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, true, 0);

        String encoded = engine.encode("any input here");

        assertEquals("", encoded);
    }

    @Test
    public void sephardicNamePrefixesAreNormalized() {
        PhoneticEngine engine = new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, false);

        String encoded = engine.encode("du della");

        assertEquals("dudela", encoded);
    }

    // Encoding API: argument validation

    @Test(expected = NullPointerException.class)
    public void encodeWithNullInputThrows() {
        new PhoneticEngine(NameType.SEPHARDIC, RuleType.EXACT, true).encode(null);
    }

    @Test(expected = NullPointerException.class)
    public void encodeWithNullLanguageSetThrows() {
        PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);

        engine.encode("abc", null);
    }

    // PhonemeBuilder: basic behavior

    @Test
    public void phonemeBuilderStartsEmptyAndAppendsText() {
        PhoneticEngine.PhonemeBuilder builder = PhoneticEngine.PhonemeBuilder.empty(Languages.ANY_LANGUAGE);

        assertEquals("", builder.makeString());

        builder.append("abc");

        assertEquals("abc", builder.makeString());
        assertEquals(1, builder.getPhonemes().size());
    }
}