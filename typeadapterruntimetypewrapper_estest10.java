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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest10 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        JsonSerializer<Object> jsonSerializer0 = (JsonSerializer<Object>) mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        JsonDeserializer<Object> jsonDeserializer0 = (JsonDeserializer<Object>) mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        Gson gson0 = new Gson();
        Class<Object> class0 = Object.class;
        Class<Integer> class1 = Integer.class;
        TypeToken<Integer> typeToken0 = TypeToken.get(class1);
        TypeAdapterFactory typeAdapterFactory0 = TreeTypeAdapter.newFactory(typeToken0, jsonSerializer0);
        TypeToken<Object> typeToken1 = TypeToken.get(class0);
        TreeTypeAdapter<Object> treeTypeAdapter0 = new TreeTypeAdapter<Object>((JsonSerializer<Object>) null, jsonDeserializer0, gson0, typeToken1, typeAdapterFactory0);
        Class<Byte> class2 = Byte.TYPE;
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<Object>(gson0, treeTypeAdapter0, class2);
        CharArrayWriter charArrayWriter0 = new CharArrayWriter();
        JsonWriter jsonWriter0 = new JsonWriter(charArrayWriter0);
        // Undeclared exception!
        try {
            typeAdapterRuntimeTypeWrapper0.write(jsonWriter0, treeTypeAdapter0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Attempted to serialize java.lang.Class: java.lang.Integer. Forgot to register a type adapter?
            // See https://github.com/google/gson/blob/main/Troubleshooting.md#java-lang-class-unsupported
            //
            verifyException("com.google.gson.internal.bind.TypeAdapters$1", e);
        }
    }
}
