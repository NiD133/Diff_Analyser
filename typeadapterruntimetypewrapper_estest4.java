package com.google.gson.internal.bind;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.temporal.ChronoField;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.junit.runner.RunWith;

public class TypeAdapterRuntimeTypeWrapper_ESTestTest4 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Gson gson0 = new Gson();
        Class<Object> class0 = Object.class;
        TypeAdapter<Object> typeAdapter0 = gson0.getAdapter(class0);
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<Object>(gson0, typeAdapter0, class0);
        StringWriter stringWriter0 = new StringWriter(2099);
        MockPrintWriter mockPrintWriter0 = new MockPrintWriter(stringWriter0);
        JsonWriter jsonWriter0 = new JsonWriter(mockPrintWriter0);
        JsonWriter jsonWriter1 = jsonWriter0.nullValue();
        // Undeclared exception!
        try {
            typeAdapterRuntimeTypeWrapper0.write(jsonWriter1, stringWriter0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // JSON must have only one top-level value.
            //
            verifyException("com.google.gson.stream.JsonWriter", e);
        }
    }
}