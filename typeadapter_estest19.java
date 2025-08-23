package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TypeAdapter_ESTestTest19 extends TypeAdapter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Gson.FutureTypeAdapter<Object> gson_FutureTypeAdapter0 = new Gson.FutureTypeAdapter<Object>();
        TypeAdapter<Object> typeAdapter0 = gson_FutureTypeAdapter0.nullSafe();
        try {
            typeAdapter0.fromJson("CU^I6g<Up/h/ru@&");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 1 path $
            // See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json
            //
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }
}
