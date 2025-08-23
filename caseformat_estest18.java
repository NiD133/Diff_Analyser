package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest18 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_UNDERSCORE;
        CaseFormat caseFormat1 = CaseFormat.UPPER_UNDERSCORE;
        String string0 = caseFormat0.to(caseFormat1, "Q#&'.zTN&p_");
        assertEquals("Q#&'.ZTN&P_", string0);
    }
}
