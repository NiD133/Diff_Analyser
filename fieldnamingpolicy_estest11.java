package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest11 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        FieldNamingPolicy fieldNamingPolicy0 = FieldNamingPolicy.UPPER_CAMEL_CASE;
        // Undeclared exception!
        try {
            fieldNamingPolicy0.translateName((Field) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.FieldNamingPolicy$2", e);
        }
    }
}
