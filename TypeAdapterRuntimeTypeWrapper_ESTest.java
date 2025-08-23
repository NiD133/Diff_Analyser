package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.*;

import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TypeAdapterRuntimeTypeWrapper_ESTest extends TypeAdapterRuntimeTypeWrapper_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testWriteChronoField() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<ChronoField> typeAdapter = gson.getAdapter(ChronoField.class);
        TypeAdapterRuntimeTypeWrapper<ChronoField> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);
        BufferedWriter bufferedWriter = new BufferedWriter(new PipedWriter());
        JsonWriter jsonWriter = gson.newJsonWriter(bufferedWriter);
        ChronoField chronoField = ChronoField.HOUR_OF_AMPM;

        // Act
        wrapper.write(jsonWriter, chronoField);

        // Assert
        assertFalse(jsonWriter.isLenient());
    }

    @Test(timeout = 4000)
    public void testReadWithTreeTypeAdapter() throws Throwable {
        // Arrange
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        Gson gson = new Gson();
        TypeAdapterFactory typeAdapterFactory = TreeTypeAdapter.newFactory(TypeToken.get(Integer.class), jsonSerializer);
        JsonSerializer<ChronoField> chronoFieldSerializer = mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        JsonDeserializer<ChronoField> chronoFieldDeserializer = mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        TreeTypeAdapter<ChronoField> treeTypeAdapter = new TreeTypeAdapter<>(chronoFieldSerializer, chronoFieldDeserializer, gson, TypeToken.get(ChronoField.class), typeAdapterFactory);
        TypeAdapterRuntimeTypeWrapper<ChronoField> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, treeTypeAdapter, Short.TYPE);
        JsonReader jsonReader = new JsonReader(new StringReader(""));

        // Act & Assert
        wrapper.read(jsonReader);
    }

    @Test(timeout = 4000)
    public void testWriteWithMockPrintWriter() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> typeAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        MockPrintWriter mockPrintWriter = new MockPrintWriter(charArrayWriter, false);
        JsonWriter jsonWriter = new JsonWriter(charArrayWriter);

        // Act & Assert
        try {
            wrapper.write(jsonWriter, mockPrintWriter);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNullValue() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> typeAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);
        StringWriter stringWriter = new StringWriter(2099);
        MockPrintWriter mockPrintWriter = new MockPrintWriter(stringWriter);
        JsonWriter jsonWriter = new JsonWriter(mockPrintWriter);
        jsonWriter.nullValue();

        // Act & Assert
        try {
            wrapper.write(jsonWriter, stringWriter);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithExcluder() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        Excluder excluder = new Excluder();
        TypeAdapter<Object> typeAdapter = gson.getDelegateAdapter(excluder, TypeToken.get(Object.class));
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Integer.TYPE);
        PipedWriter pipedWriter = new PipedWriter();
        JsonWriter jsonWriter = new JsonWriter(pipedWriter);

        // Act & Assert
        try {
            wrapper.write(jsonWriter, TypeToken.get(Integer.TYPE));
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithGsonString() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> typeAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);
        StringReader stringReader = new StringReader(gson.toString());
        JsonReader jsonReader = gson.newJsonReader(stringReader);

        // Act & Assert
        try {
            wrapper.read(jsonReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNullJsonReader() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> typeAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);

        // Act & Assert
        try {
            wrapper.read(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithEmptyStringReader() throws Throwable {
        // Arrange
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        Gson gson = new Gson();
        TypeAdapterFactory typeAdapterFactory = TreeTypeAdapter.newFactoryWithMatchRawType(TypeToken.get(Object.class), jsonSerializer);
        TypeAdapter<Object> typeAdapter = gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get(Object.class));
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);
        JsonReader jsonReader = gson.newJsonReader(new StringReader(""));

        // Act & Assert
        try {
            wrapper.read(jsonReader);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithPipedReader() throws Throwable {
        // Arrange
        JsonSerializer<Integer> jsonSerializer = mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        JsonDeserializer<Integer> jsonDeserializer = mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        TypeAdapterFactory typeAdapterFactory = DefaultDateTypeAdapter.DEFAULT_STYLE_FACTORY;
        TreeTypeAdapter<Integer> treeTypeAdapter = new TreeTypeAdapter<>(jsonSerializer, jsonDeserializer, null, TypeToken.get(Integer.class), typeAdapterFactory, true);
        TypeAdapterRuntimeTypeWrapper<Integer> wrapper = new TypeAdapterRuntimeTypeWrapper<>(null, treeTypeAdapter, Integer.class);
        JsonReader jsonReader = new JsonReader(new PipedReader());

        // Act & Assert
        try {
            wrapper.read(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithUnsupportedOperation() throws Throwable {
        // Arrange
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        Gson gson = new Gson();
        TypeAdapterFactory typeAdapterFactory = TreeTypeAdapter.newFactory(TypeToken.get(Integer.class), jsonSerializer);
        TreeTypeAdapter<Object> treeTypeAdapter = new TreeTypeAdapter<>(null, jsonDeserializer, gson, TypeToken.get(Object.class), typeAdapterFactory);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, treeTypeAdapter, Byte.TYPE);
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        JsonWriter jsonWriter = new JsonWriter(charArrayWriter);

        // Act & Assert
        try {
            wrapper.write(jsonWriter, treeTypeAdapter);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNullJsonWriter() throws Throwable {
        // Arrange
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(jsonSerializer).serialize(any(), any(java.lang.reflect.Type.class), any(com.google.gson.JsonSerializationContext.class));
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        Gson gson = new Gson();
        TypeAdapterFactory typeAdapterFactory = TreeTypeAdapter.newFactory(TypeToken.get(Object.class), jsonSerializer);
        TreeTypeAdapter<Object> treeTypeAdapter = new TreeTypeAdapter<>(jsonSerializer, jsonDeserializer, gson, TypeToken.get(Object.class), typeAdapterFactory);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, treeTypeAdapter, Short.TYPE);

        // Act & Assert
        try {
            wrapper.write(null, gson);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithReflectiveTypeAdapter() throws Throwable {
        // Arrange
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class, new ViolatedAssumptionAnswer());
        Gson gson = new Gson();
        TypeAdapterFactory typeAdapterFactory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
        TreeTypeAdapter<Object> treeTypeAdapter = new TreeTypeAdapter<>(null, jsonDeserializer, gson, TypeToken.get(Object.class), typeAdapterFactory);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, treeTypeAdapter, Short.TYPE);

        // Act & Assert
        try {
            wrapper.write(null, TypeToken.get(Object.class));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteWithNullValues() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> typeAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);

        // Act & Assert
        try {
            wrapper.write(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testReadWithPipedReaderException() throws Throwable {
        // Arrange
        Gson gson = new Gson();
        TypeAdapter<Object> typeAdapter = gson.getAdapter(Object.class);
        TypeAdapterRuntimeTypeWrapper<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, typeAdapter, Object.class);
        JsonReader jsonReader = gson.newJsonReader(new PipedReader());

        // Act & Assert
        try {
            wrapper.read(jsonReader);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }
}