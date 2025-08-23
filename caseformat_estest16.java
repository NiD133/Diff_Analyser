package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest16 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.UPPER_UNDERSCORE;
        CaseFormat caseFormat1 = CaseFormat.LOWER_HYPHEN;
        String string0 = caseFormat0.convert(caseFormat1, "-");
        assertEquals("-", string0);
    }
}
