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

public class PhoneticEngine_ESTestTest29 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        NameType nameType0 = NameType.SEPHARDIC;
        RuleType ruleType0 = RuleType.APPROX;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, false);
        phoneticEngine0.getRuleType();
        assertFalse(phoneticEngine0.isConcat());
        assertEquals(20, phoneticEngine0.getMaxPhonemes());
    }
}
