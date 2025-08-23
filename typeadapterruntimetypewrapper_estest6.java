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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest6 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Gson gson0 = new Gson();
        String string0 = gson0.toString();
        Class<Object> class0 = Object.class;
        TypeAdapter<Object> typeAdapter0 = gson0.getAdapter(class0);
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<Object>(gson0, typeAdapter0, class0);
        StringReader stringReader0 = new StringReader(string0);
        JsonReader jsonReader0 = gson0.newJsonReader(stringReader0);
        try {
            typeAdapterRuntimeTypeWrapper0.read(jsonReader0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1 column 3 path $.
            // See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json
            //
            verifyException("com.google.gson.stream.JsonReader", e);
        }
    }
}
