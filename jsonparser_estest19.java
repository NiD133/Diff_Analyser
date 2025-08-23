package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonParser_ESTestTest19 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        JsonParser jsonParser0 = new JsonParser();
        // Undeclared exception!
        try {
            jsonParser0.parse((Reader) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // in == null
            //
            verifyException("java.util.Objects", e);
        }
    }
}
