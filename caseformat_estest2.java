package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest2 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_CAMEL;
        CaseFormat caseFormat1 = CaseFormat.LOWER_UNDERSCORE;
        String string0 = caseFormat0.to(caseFormat1, "0P|{HG$S{ax$v|r_ ");
        assertEquals("0_p|{_h_g$_s{ax$v|r_ ", string0);
    }
}
