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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest5 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Gson gson0 = new Gson();
        Excluder excluder0 = new Excluder();
        Class<Integer> class0 = Integer.TYPE;
        TypeToken<Integer> typeToken0 = TypeToken.get(class0);
        Class<Object> class1 = Object.class;
        TypeToken<Object> typeToken1 = TypeToken.get(class1);
        TypeAdapter<Object> typeAdapter0 = gson0.getDelegateAdapter((TypeAdapterFactory) excluder0, typeToken1);
        TypeAdapterRuntimeTypeWrapper<Object> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<Object>(gson0, typeAdapter0, class0);
        PipedWriter pipedWriter0 = new PipedWriter();
        JsonWriter jsonWriter0 = new JsonWriter(pipedWriter0);
        try {
            typeAdapterRuntimeTypeWrapper0.write(jsonWriter0, typeToken0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Pipe not connected
            //
            verifyException("java.io.PipedWriter", e);
        }
    }
}
