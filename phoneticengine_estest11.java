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

public class PhoneticEngine_ESTestTest11 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        NameType nameType0 = NameType.GENERIC;
        RuleType ruleType0 = RuleType.EXACT;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, true);
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        Languages.LanguageSet languages_LanguageSet0 = Languages.LanguageSet.from(linkedHashSet0);
        // Undeclared exception!
        phoneticEngine0.encode("de la deorg.apache.commons.codec.language.bm.languages$2", languages_LanguageSet0);
    }
}
