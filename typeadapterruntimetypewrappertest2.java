package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import org.junit.Test;

/**
 * Tests for how Gson handles serialization of runtime types, specifically focusing on the behavior
 * of {@code TypeAdapterRuntimeTypeWrapper}.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

    // A simple base class for polymorphism testing.
    private static class Shape {}

    // A subclass with a specific field to verify its serializer is used.
    private static class Circle extends Shape {
        @SuppressWarnings("unused")
        String type = "circle";
    }

    // A container for the object being serialized.
    private static class Container {
        // The field has a declared type of Shape but will hold a Circle instance at runtime.
        @SuppressWarnings("unused")
        Shape value = new Circle();
    }

    /**
     * A custom adapter that ONLY implements deserialization for the Shape class.
     * Its presence is crucial for setting up the test scenario.
     */
    private static class ShapeDeserializer implements JsonDeserializer<Shape> {
        @Override
        public Shape deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            // This test focuses on serialization, so this method should never be called.
            throw new AssertionError("Deserializer should not be called during this serialization test.");
        }
    }

    /**
     * Tests that serialization uses the runtime type's reflective adapter when only a
     * deserializer is registered for the declared base type.
     */
    @Test
    public void serializationUsesRuntimeTypeWhenOnlyDeserializerIsRegisteredForBaseType() {
        // Arrange: Create a Gson instance where the base `Shape` type has a custom deserializer,
        // but no custom serializer.
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Shape.class, new ShapeDeserializer())
                .create();

        Container container = new Container();

        // Act: Serialize the container. Gson should detect that the `value` field's runtime
        // type is `Circle`, not `Shape`. Since no specific serializer is registered for `Shape`,
        // Gson should use the default reflective serializer for `Circle`.
        String json = gson.toJson(container);

        // Assert: The JSON output must be based on the `Circle` runtime type, including its
        // fields. This confirms that Gson used the correct, more specific type adapter.
        assertThat(json).isEqualTo("{\"value\":{\"type\":\"circle\"}}");
    }
}