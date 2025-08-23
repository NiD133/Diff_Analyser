package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest13 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_CAMEL;
        String string0 = caseFormat0.convert(caseFormat0, "UPPER_UNDERSCORE");
        assertEquals("uPPER_UNDERSCORE", string0);
    }
}
