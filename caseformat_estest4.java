package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest4 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.UPPER_CAMEL;
        CaseFormat caseFormat1 = CaseFormat.LOWER_UNDERSCORE;
        String string0 = caseFormat1.to(caseFormat0, "bDU\"5");
        assertEquals("Bdu\"5", string0);
    }
}
