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

public class JsonParser_ESTestTest16 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        StringReader stringReader0 = new StringReader("R3P?4");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        JsonParser.parseReader(jsonReader0);
        // Undeclared exception!
        try {
            JsonParser.parseReader(jsonReader0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Unexpected token: END_DOCUMENT
            //
            verifyException("com.google.gson.internal.bind.JsonElementTypeAdapter", e);
        }
    }
}
