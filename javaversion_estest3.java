package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JavaVersion_ESTestTest3 extends JavaVersion_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        boolean boolean0 = JavaVersion.isJava9OrLater();
        assertFalse(boolean0);
    }
}
