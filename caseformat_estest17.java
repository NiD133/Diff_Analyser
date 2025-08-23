package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest17 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_CAMEL;
        CaseFormat caseFormat1 = CaseFormat.UPPER_UNDERSCORE;
        // Undeclared exception!
        try {
            caseFormat1.convert(caseFormat0, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.CharMatcher", e);
        }
    }
}
