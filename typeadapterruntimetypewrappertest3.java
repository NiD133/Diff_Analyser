package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.Test;

/**
 * Tests for polymorphic serialization where a custom adapter is registered for a base type.
 */
public class PolymorphicTypeAdapterTest {

    // Test model classes
    private static class Shape {}

    private static class Circle extends Shape {
        @SuppressWarnings("unused")
        String properties = "is_round";
    }

    private static class Drawing {
        @SuppressWarnings("unused")
        Shape shape = new Circle();
    }

    /**
     * A custom TypeAdapter for the Shape base class. It serializes any Shape
     * instance to a fixed string, ignoring its actual runtime type.
     */
    private static class CustomShapeSerializer extends TypeAdapter<Shape> {
        static final String SERIALIZED_VALUE = "custom-shape-serializer";

        @Override
        public void write(JsonWriter out, Shape value) throws IOException {
            out.value(SERIALIZED_VALUE);
        }

        @Override
        public Shape read(JsonReader in) {
            throw new UnsupportedOperationException("Not needed for this serialization test");
        }
    }

    /**
     * A deserializer that is registered but never called. Its presence alongside a
     * TypeAdapter for the same type is necessary to trigger the specific internal
     * code path in Gson that this test aims to verify.
     */
    private static class UnusedShapeDeserializer implements JsonDeserializer<Shape> {
        @Override
        public Shape deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            throw new AssertionError("This deserializer should not be called during serialization");
        }
    }

    /**
     * This test verifies that when a custom {@link TypeAdapter} is registered for a base class,
     * Gson prefers it for serializing subclass instances over the default reflective adapter.
     *
     * This holds true even when a {@link JsonDeserializer} is also registered for the same
     * base class, which internally causes Gson to use a {@code TreeTypeAdapter}.
     */
    @Test
    public void customBaseClassAdapter_isUsed_forSubclassInstanceSerialization() {
        // Arrange: Create a Gson instance where the base class 'Shape' has a custom
        // serializer. This setup ensures we test that the custom serializer for the
        // base type takes precedence.
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Shape.class, new CustomShapeSerializer())
            .registerTypeAdapter(Shape.class, new UnusedShapeDeserializer())
            .create();

        Drawing drawing = new Drawing(); // Contains a Circle instance in a Shape field.

        // Act: Serialize the container object.
        String json = gson.toJson(drawing);

        // Assert: The output must use the custom serializer for the 'Shape' class,
        // not the default reflective serializer for the 'Circle' subclass.
        String expectedJson = "{\"shape\":\"" + CustomShapeSerializer.SERIALIZED_VALUE + "\"}";
        assertThat(json).isEqualTo(expectedJson);
    }
}