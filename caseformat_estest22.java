package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest22 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_HYPHEN;
        CaseFormat caseFormat1 = CaseFormat.LOWER_UNDERSCORE;
        String string0 = caseFormat0.to(caseFormat1, "S:^5jO-|]r");
        assertEquals("S:^5jO_|]r", string0);
    }
}
