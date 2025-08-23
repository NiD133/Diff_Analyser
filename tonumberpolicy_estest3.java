package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ToNumberPolicy_ESTestTest3 extends ToNumberPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        StringReader stringReader0 = new StringReader(".a&;-m");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        Strictness strictness0 = Strictness.LENIENT;
        jsonReader0.setStrictness(strictness0);
        ToNumberPolicy toNumberPolicy0 = ToNumberPolicy.LONG_OR_DOUBLE;
        // Undeclared exception!
        try {
            toNumberPolicy0.readNumber(jsonReader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Cannot parse .a&; at path $
            //
            verifyException("com.google.gson.ToNumberPolicy$3", e);
        }
    }
}