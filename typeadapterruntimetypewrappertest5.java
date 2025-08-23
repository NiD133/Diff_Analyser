package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.junit.Test;

/**
 * Tests for {@code TypeAdapterRuntimeTypeWrapper}, focusing on serialization behavior with
 * polymorphic types.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

  // A simple base class for polymorphism testing.
  private static class Shape {}

  // A subclass to test runtime type handling.
  private static class Circle extends Shape {
    @SuppressWarnings("unused")
    final String type = "circle";
  }

  // A container holding a field of the base type, but instantiated with a subclass.
  private static class Drawing {
    @SuppressWarnings("unused")
    Shape shape = new Circle();
  }

  /**
   * This test verifies that when a custom serializer is registered for a base class (e.g.,
   * {@code Shape}), Gson uses that specific serializer for instances of its subclasses (e.g.,
   * {@code Circle}).
   *
   * <p>The expected behavior is that Gson does not fall back to the default reflection-based
   * adapter for the subclass, but instead honors the custom serializer registered for the base
   * type.
   */
  @Test
  public void serializationUsesBaseTypeSerializerForSubclassInstance() {
    // Arrange
    JsonSerializer<Shape> shapeSerializer =
        (src, typeOfSrc, context) -> new JsonPrimitive("custom-shape-serializer");

    // A dummy deserializer is also registered for the base type. This is a technical requirement
    // to ensure Gson creates a TreeTypeAdapter, which in turn gets wrapped by
    // TypeAdapterRuntimeTypeWrapper, the class whose behavior we are testing.
    JsonDeserializer<Shape> dummyDeserializer =
        (json, typeOfT, context) -> {
          throw new AssertionError("Deserializer should not be called during serialization");
        };

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Shape.class, shapeSerializer)
            .registerTypeAdapter(Shape.class, dummyDeserializer)
            .create();

    Drawing drawing = new Drawing();
    String expectedJson = "{\"shape\":\"custom-shape-serializer\"}";

    // Act
    String actualJson = gson.toJson(drawing);

    // Assert
    assertThat(actualJson).isEqualTo(expectedJson);
  }
}