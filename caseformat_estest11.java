package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest11 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_CAMEL;
        // Undeclared exception!
        try {
            caseFormat0.converterTo((CaseFormat) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
