package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ToNumberPolicy_ESTestTest5 extends ToNumberPolicy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test4() throws Throwable {
        ToNumberPolicy toNumberPolicy0 = ToNumberPolicy.LAZILY_PARSED_NUMBER;
        // Undeclared exception!
        try {
            toNumberPolicy0.readNumber((JsonReader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.gson.ToNumberPolicy$2", e);
        }
    }
}
