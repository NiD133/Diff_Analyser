package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest15 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_UNDERSCORE;
        CaseFormat caseFormat1 = CaseFormat.UPPER_UNDERSCORE;
        String string0 = caseFormat1.convert(caseFormat0, "2$ddmbvc\" rj0j %>[");
        assertEquals("2$ddmbvc\" rj0j %>[", string0);
    }
}
