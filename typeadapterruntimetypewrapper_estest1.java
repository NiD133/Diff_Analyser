package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;

public class TypeAdapterRuntimeTypeWrapper_ESTestTest1 {

    /**
     * Tests that the {@link TypeAdapterRuntimeTypeWrapper#write(JsonWriter, Object)} method
     * correctly serializes an object using the adapter for its specific runtime type,
     * even when the wrapper is configured with a more general declared type.
     *
     * <p><b>Scenario:</b>
     * <ul>
     *     <li>The wrapper is created for the general type {@code Object.class}.
     *     <li>The value to be serialized is an enum constant {@code ChronoField.HOUR_OF_AMPM}.
     *     <li>The wrapper should detect that the runtime type ({@code ChronoField}) is more
     *         specific than the declared type ({@code Object}) and use Gson's default
     *         enum adapter for serialization.
     * </ul>
     * </p>
     */
    @Test
    public void write_whenRuntimeTypeIsMoreSpecific_usesRuntimeTypeAdapter() throws IOException {
        // Arrange
        Gson gson = new Gson();
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

        // The declared type is a general supertype (Object).
        Type declaredType = Object.class;
        // The delegate adapter is the one for the declared type.
        TypeAdapter<Object> delegateAdapter = gson.getAdapter(Object.class);

        // Create the wrapper under test. It's responsible for handling runtime type differences.
        TypeAdapter<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, declaredType);

        // The actual value has a more specific runtime type (ChronoField) than the declared type.
        Object value = ChronoField.HOUR_OF_AMPM;

        // Act
        // The wrapper should detect the runtime type and use the specific adapter for ChronoField.
        wrapper.write(jsonWriter, value);

        // Assert
        // Gson's default adapter for enums serializes them to their name as a JSON string.
        String expectedJson = "\"HOUR_OF_AMPM\"";
        assertEquals(expectedJson, stringWriter.toString());
    }
}