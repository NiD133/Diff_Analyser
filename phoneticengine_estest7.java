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

public class PhoneticEngine_ESTestTest7 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        RuleType ruleType0 = RuleType.APPROX;
        NameType nameType0 = NameType.ASHKENAZI;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, false, (-1141));
        // Undeclared exception!
        try {
            phoneticEngine0.encode("c)mY)");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal initial capacity: -1141
            //
            verifyException("java.util.HashMap", e);
        }
    }
}
