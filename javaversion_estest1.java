package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JavaVersion_ESTestTest1 extends JavaVersion_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        int int0 = JavaVersion.parseMajorJavaVersion("0Q?");
        assertEquals(0, int0);
    }
}
