package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.LinkedHashSet;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PhoneticEngine_ESTestTest16 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        linkedHashSet0.add("common");
        Languages.LanguageSet languages_LanguageSet0 = Languages.LanguageSet.from(linkedHashSet0);
        PhoneticEngine.PhonemeBuilder phoneticEngine_PhonemeBuilder0 = PhoneticEngine.PhonemeBuilder.empty(languages_LanguageSet0);
        CharBuffer charBuffer0 = CharBuffer.allocate(0);
        Rule.Phoneme rule_Phoneme0 = new Rule.Phoneme(charBuffer0, languages_LanguageSet0);
        phoneticEngine_PhonemeBuilder0.apply(rule_Phoneme0, 0);
        assertEquals(1, rule_Phoneme0.size());
    }
}
