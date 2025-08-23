package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Field;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class FieldNamingPolicy_ESTestTest3 extends FieldNamingPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        // Undeclared exception!
        try {
            FieldNamingPolicy.upperCaseFirstLetter((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.FieldNamingPolicy", e);
        }
    }
}
