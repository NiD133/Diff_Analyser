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

public class TypeAdapterRuntimeTypeWrapper_ESTestTest1 extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Gson gson0 = new Gson();
        Class<Object> class0 = Object.class;
        Class<ChronoField> class1 = ChronoField.class;
        TypeAdapter<ChronoField> typeAdapter0 = gson0.getAdapter(class1);
        TypeAdapterRuntimeTypeWrapper<ChronoField> typeAdapterRuntimeTypeWrapper0 = new TypeAdapterRuntimeTypeWrapper<ChronoField>(gson0, typeAdapter0, class0);
        PipedWriter pipedWriter0 = new PipedWriter();
        BufferedWriter bufferedWriter0 = new BufferedWriter(pipedWriter0);
        JsonWriter jsonWriter0 = gson0.newJsonWriter(bufferedWriter0);
        ChronoField chronoField0 = ChronoField.HOUR_OF_AMPM;
        typeAdapterRuntimeTypeWrapper0.write(jsonWriter0, chronoField0);
        assertFalse(jsonWriter0.isLenient());
    }
}
