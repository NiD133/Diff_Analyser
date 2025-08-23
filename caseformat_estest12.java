package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest12 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_HYPHEN;
        CaseFormat caseFormat1 = CaseFormat.UPPER_CAMEL;
        String string0 = caseFormat0.to(caseFormat1, "");
        assertEquals("", string0);
    }
}
