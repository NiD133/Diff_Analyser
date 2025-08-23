package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest1 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_CAMEL;
        CaseFormat caseFormat1 = CaseFormat.UPPER_UNDERSCORE;
        String string0 = caseFormat1.to(caseFormat0, "Y3:E]9bSR@H%B/_?");
        assertEquals("y3:e]9bsr@h%b/?", string0);
    }
}
