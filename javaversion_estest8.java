package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JavaVersion_ESTestTest8 extends JavaVersion_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test7() throws Throwable {
        int int0 = JavaVersion.getMajorJavaVersion();
        assertEquals(8, int0);
    }
}
