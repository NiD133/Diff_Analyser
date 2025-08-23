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

public class PhoneticEngine_ESTestTest31 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        NameType nameType0 = NameType.GENERIC;
        RuleType ruleType0 = RuleType.EXACT;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, true);
        phoneticEngine0.getNameType();
        assertTrue(phoneticEngine0.isConcat());
        assertEquals(20, phoneticEngine0.getMaxPhonemes());
    }
}
