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

public class TypeAdapter_ESTestTest3 extends TypeAdapter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Gson.FutureTypeAdapter<Object> gson_FutureTypeAdapter0 = new Gson.FutureTypeAdapter<Object>();
        TypeAdapter<Object> typeAdapter0 = gson_FutureTypeAdapter0.nullSafe();
        String string0 = typeAdapter0.toJson((Object) null);
        assertEquals("null", string0);
    }
}
