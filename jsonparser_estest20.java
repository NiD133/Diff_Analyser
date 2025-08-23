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

public class JsonParser_ESTestTest20 extends JsonParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        JsonParser jsonParser0 = new JsonParser();
        StringReader stringReader0 = new StringReader("}Yz");
        try {
            jsonParser0.parse((Reader) stringReader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // org.evosuite.runtime.mock.java.lang.MockThrowable: Expected value at line 1 column 1 path $
            // See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json
            //
            verifyException("com.google.gson.internal.Streams", e);
        }
    }
}
