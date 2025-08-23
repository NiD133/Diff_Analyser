package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ToNumberPolicy_ESTestTest1 extends ToNumberPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
        StringReader stringReader0 = new StringReader("\"*.\"{/");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        ToNumberPolicy toNumberPolicy0 = ToNumberPolicy.LONG_OR_DOUBLE;
        // Undeclared exception!
        try {
            toNumberPolicy0.readNumber(jsonReader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Cannot parse *.; at path $
            //
            verifyException("com.google.gson.ToNumberPolicy$3", e);
        }
    }
}
