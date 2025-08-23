package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JavaVersion_ESTestTest2 extends JavaVersion_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        // Undeclared exception!
        try {
            JavaVersion.parseMajorJavaVersion((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.internal.JavaVersion", e);
        }
    }
}
