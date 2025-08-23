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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest2 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        JsonSerializer<Object> jsonSerializer0 = (JsonSerializer<Object>) mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        Gson gson0 = new Gson();
        Class<Short> class0 = Short.TYPE;
        Class<Integer> class1 = Integer.class;
        TypeToken<Integer> typeToken0 = TypeToken.get(class1);
        TypeAdapterFactory typeAdapterFactory0 = TreeTypeAdapter.newFactory(typeToken0, jsonSerializer0);
        Class<ChronoField> class2 = ChronoField.class;
        TypeToken<ChronoField> typeToken1 = TypeToken.get(class2);
        JsonSerializer<ChronoField> jsonSerializer1 = (JsonSerializer<ChronoField>) mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        JsonDeserializer<ChronoField> jsonDeserializer0 = (JsonDeserializer<ChronoField>) mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        TreeTypeAdapter<ChronoField> treeTypeAdapter0 = new TreeTypeAdapter<ChronoField>(jsonSerializer1, jsonDeserializer0, gson0, typeToken1, typeAdapterFactory0);
        TypeAdapterRuntimeTypeWrapper<ChronoField> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<ChronoField>(gson0, treeTypeAdapter0, class0);
        StringReader stringReader0 = new StringReader("");
        JsonReader jsonReader0 = new JsonReader(stringReader0);
        typeAdapterRuntimeTypeWrapper0.read(jsonReader0);
    }
}
