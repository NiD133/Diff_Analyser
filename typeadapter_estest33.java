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

public class TypeAdapter_ESTestTest33 extends TypeAdapter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Gson.FutureTypeAdapter<Object> gson_FutureTypeAdapter0 = new Gson.FutureTypeAdapter<Object>();
        TypeAdapter<Object> typeAdapter0 = gson_FutureTypeAdapter0.nullSafe();
        // Undeclared exception!
        try {
            typeAdapter0.toJsonTree(gson_FutureTypeAdapter0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Adapter for type with cyclic dependency has been used before dependency has been resolved
            //
            verifyException("com.google.gson.Gson$FutureTypeAdapter", e);
        }
    }
}
