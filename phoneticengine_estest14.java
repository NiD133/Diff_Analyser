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

public class PhoneticEngine_ESTestTest14 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        Languages.LanguageSet languages_LanguageSet0 = Languages.LanguageSet.from(linkedHashSet0);
        NameType nameType0 = NameType.SEPHARDIC;
        RuleType ruleType0 = RuleType.APPROX;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, true, (-1));
        // Undeclared exception!
        try {
            phoneticEngine0.encode("des phonme expression contains a '[' but does not end in ']'", languages_LanguageSet0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal initial capacity: -1
            //
            verifyException("java.util.HashMap", e);
        }
    }
}
