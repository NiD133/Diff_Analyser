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

public class PhoneticEngine_ESTestTest25 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        NameType nameType0 = NameType.ASHKENAZI;
        RuleType ruleType0 = RuleType.RULES;
        PhoneticEngine phoneticEngine0 = null;
        try {
            phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, false);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // ruleType must not be RULES
            //
            verifyException("org.apache.commons.codec.language.bm.PhoneticEngine", e);
        }
    }
}
