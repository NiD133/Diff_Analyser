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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest8 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        JsonSerializer<Object> jsonSerializer0 = (JsonSerializer<Object>) mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        Gson gson0 = new Gson();
        Class<Object> class0 = Object.class;
        TypeToken<Object> typeToken0 = TypeToken.get(class0);
        TypeAdapterFactory typeAdapterFactory0 = TreeTypeAdapter.newFactoryWithMatchRawType(typeToken0, jsonSerializer0);
        TypeAdapter<Object> typeAdapter0 = gson0.getDelegateAdapter(typeAdapterFactory0, typeToken0);
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<Object>(gson0, typeAdapter0, class0);
        StringReader stringReader0 = new StringReader("");
        JsonReader jsonReader0 = gson0.newJsonReader(stringReader0);
        try {
            typeAdapterRuntimeTypeWrapper0.read(jsonReader0);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            //
            // End of input at line 1 column 1 path $
            //
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }
}
