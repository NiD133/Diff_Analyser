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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest9 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        JsonSerializer<Integer> jsonSerializer0 = (JsonSerializer<Integer>) mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        JsonDeserializer<Integer> jsonDeserializer0 = (JsonDeserializer<Integer>) mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        Class<Integer> class0 = Integer.class;
        TypeToken<Integer> typeToken0 = TypeToken.get(class0);
        TypeAdapterFactory typeAdapterFactory0 = DefaultDateTypeAdapter.DEFAULT_STYLE_FACTORY;
        TreeTypeAdapter<Integer> treeTypeAdapter0 = new TreeTypeAdapter<Integer>(jsonSerializer0, jsonDeserializer0, (Gson) null, typeToken0, typeAdapterFactory0, true);
        TypeAdapterRuntimeTypeWrapper<Integer> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<Integer>((Gson) null, treeTypeAdapter0, class0);
        PipedReader pipedReader0 = new PipedReader();
        JsonReader jsonReader0 = new JsonReader(pipedReader0);
        // Undeclared exception!
        try {
            typeAdapterRuntimeTypeWrapper0.read(jsonReader0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // java.io.IOException: Pipe not connected
            //
            verifyException("com.google.gson.internal.Streams", e);
        }
    }
}
