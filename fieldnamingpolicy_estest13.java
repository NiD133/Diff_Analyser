package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest13 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        FieldNamingPolicy fieldNamingPolicy0 = FieldNamingPolicy.valueOf("LOWER_CASE_WITH_UNDERSCORES");
        // Undeclared exception!
        try {
            fieldNamingPolicy0.translateName((Field) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.FieldNamingPolicy$5", e);
        }
    }
}
