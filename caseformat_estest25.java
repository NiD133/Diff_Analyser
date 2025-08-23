package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest25 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        CaseFormat caseFormat0 = CaseFormat.LOWER_CAMEL;
        Converter<String, String> converter0 = caseFormat0.converterTo(caseFormat0);
        assertNotNull(converter0);
    }
}
