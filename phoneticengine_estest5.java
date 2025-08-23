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

public class PhoneticEngine_ESTestTest5 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        NameType nameType0 = NameType.GENERIC;
        RuleType ruleType0 = RuleType.EXACT;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, false);
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        linkedHashSet0.add("de la daorg.apache.commons.codec.language.bm.languages$languageset");
        Languages.LanguageSet languages_LanguageSet0 = Languages.LanguageSet.from(linkedHashSet0);
        // Undeclared exception!
        try {
            phoneticEngine0.encode("de la daorg.apache.commons.codec.language.bm.languages$languageset", languages_LanguageSet0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No rules found for gen, rules, de la daorg.apache.commons.codec.language.bm.languages$languageset.
            //
            verifyException("org.apache.commons.codec.language.bm.Rule", e);
        }
    }
}
