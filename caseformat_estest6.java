package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CaseFormat_ESTestTest6 extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        CaseFormat.valueOf("LOWER_CAMEL");
    }
}
