/*
 * Copyright (C) 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.Test;

/**
 * Tests for TypeAdapterRuntimeTypeWrapper behavior when serializing objects with inheritance.
 * 
 * The TypeAdapterRuntimeTypeWrapper determines which adapter to use when serializing objects
 * at runtime, especially when dealing with inheritance hierarchies and custom adapters.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

  // Test data classes representing inheritance hierarchy
  private static class BaseClass {}

  private static class SubClass extends BaseClass {
    @SuppressWarnings("unused")
    String fieldValue = "test";
  }

  private static class ObjectContainer {
    @SuppressWarnings("unused")
    BaseClass baseField = new SubClass(); // Contains subclass instance
  }

  // Helper classes for testing custom adapters
  private static class NoOpDeserializer implements JsonDeserializer<BaseClass> {
    @Override
    public BaseClass deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      throw new AssertionError("Deserializer not needed for serialization tests");
    }
  }

  private static JsonSerializer<BaseClass> createCustomSerializer(String output) {
    return (src, typeOfSrc, context) -> new JsonPrimitive(output);
  }

  private static TypeAdapter<BaseClass> createCustomTypeAdapter(String output) {
    return new TypeAdapter<BaseClass>() {
      @Override
      public BaseClass read(JsonReader in) throws IOException {
        throw new UnsupportedOperationException("Read not implemented for test");
      }

      @Override
      public void write(JsonWriter out, BaseClass value) throws IOException {
        out.value(output);
      }
    };
  }

  @Test
  public void serialization_withCustomJsonSerializer_shouldUseCustomSerializer() {
    // Given: Gson configured with custom JsonSerializer for base class
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(BaseClass.class, createCustomSerializer("custom-serializer"))
        .create();

    // When: Serializing container with subclass instance
    String actualJson = gson.toJson(new ObjectContainer());

    // Then: Should use custom serializer instead of reflective adapter for subclass
    String expectedJson = "{\"baseField\":\"custom-serializer\"}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void serialization_withOnlyJsonDeserializer_shouldUseReflectiveSerializer() {
    // Given: Gson configured with only JsonDeserializer (no custom serializer)
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(BaseClass.class, new NoOpDeserializer())
        .create();

    // When: Serializing container with subclass instance
    String actualJson = gson.toJson(new ObjectContainer());

    // Then: Should use reflective serializer for subclass (showing subclass fields)
    String expectedJson = "{\"baseField\":{\"fieldValue\":\"test\"}}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void serialization_withDeserializerAndCustomTypeAdapter_shouldUseCustomTypeAdapter() {
    // Given: Gson with custom TypeAdapter as delegate and JsonDeserializer
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(BaseClass.class, createCustomTypeAdapter("custom-type-adapter"))
        .registerTypeAdapter(BaseClass.class, new NoOpDeserializer())
        .create();

    // When: Serializing container with subclass instance
    String actualJson = gson.toJson(new ObjectContainer());

    // Then: Should prefer custom TypeAdapter over reflective adapter
    String expectedJson = "{\"baseField\":\"custom-type-adapter\"}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void serialization_withMultipleDeserializers_shouldUseReflectiveSerializer() {
    // Given: Gson with multiple JsonDeserializers (both fall back to reflective serialization)
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(BaseClass.class, new NoOpDeserializer())
        .registerTypeAdapter(BaseClass.class, new NoOpDeserializer())
        .create();

    // When: Serializing container with subclass instance
    String actualJson = gson.toJson(new ObjectContainer());

    // Then: Should use reflective serializer for subclass
    String expectedJson = "{\"baseField\":{\"fieldValue\":\"test\"}}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void serialization_withDeserializerAndJsonSerializer_shouldUseJsonSerializer() {
    // Given: Gson with JsonSerializer as delegate and JsonDeserializer
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(BaseClass.class, createCustomSerializer("json-serializer-delegate"))
        .registerTypeAdapter(BaseClass.class, new NoOpDeserializer())
        .create();

    // When: Serializing container with subclass instance
    String actualJson = gson.toJson(new ObjectContainer());

    // Then: Should prefer JsonSerializer over reflective adapter
    String expectedJson = "{\"baseField\":\"json-serializer-delegate\"}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void serialization_withSubclassDeserializer_shouldMaintainBackwardCompatibility() {
    // Given: JsonDeserializer for subclass AND JsonSerializer for base class
    // This tests backward compatibility behavior from GitHub issue #1787
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SubClass.class, 
            (JsonDeserializer<SubClass>) (json, typeOfT, context) -> {
              throw new AssertionError("Deserializer not needed for serialization tests");
            })
        .registerTypeAdapter(BaseClass.class, createCustomSerializer("base-serializer"))
        .create();

    // When: Serializing container with subclass instance
    String actualJson = gson.toJson(new ObjectContainer());

    // Then: Should use reflective adapter for backward compatibility
    // (even though custom serializer exists for base class)
    String expectedJson = "{\"baseField\":{\"fieldValue\":\"test\"}}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  // Test classes for cyclic reference scenario
  private static class CyclicParent {
    @SuppressWarnings("unused")
    CyclicParent childField;
  }

  private static class CyclicChild extends CyclicParent {
    @SuppressWarnings("unused")
    int value;

    CyclicChild(int value) {
      this.value = value;
    }
  }

  @Test
  public void serialization_withCyclicReferences_shouldHandleGsonFutureAdapter() {
    // Given: Object with cyclic reference where field type is currently being processed
    CyclicParent parent = new CyclicParent();
    parent.childField = new CyclicChild(42);

    // When: Serializing with default Gson (tests future adapter behavior)
    String actualJson = new Gson().toJson(parent);

    // Then: Should successfully serialize using future adapter mechanism
    String expectedJson = "{\"childField\":{\"value\":42}}";
    assertThat(actualJson).isEqualTo(expectedJson);
  }
}