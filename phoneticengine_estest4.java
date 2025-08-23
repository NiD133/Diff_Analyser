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

public class PhoneticEngine_ESTestTest4 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        RuleType ruleType0 = RuleType.EXACT;
        NameType nameType0 = NameType.GENERIC;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, false);
        // Undeclared exception!
        try {
            phoneticEngine0.encode("8C?#]", (Languages.LanguageSet) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.codec.language.bm.Rule", e);
        }
    }
}
