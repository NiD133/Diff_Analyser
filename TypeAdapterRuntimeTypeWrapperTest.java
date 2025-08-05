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

public class TypeAdapterRuntimeTypeWrapperTest {
  // Simple base class for inheritance testing
  private static class Base {}

  // Subclass with a field to verify reflective serialization
  private static class Subclass extends Base {
    @SuppressWarnings("unused")
    String value = "test";
  }

  // Container holding a Base-typed field (typically set to Subclass instance)
  private static class Container {
    @SuppressWarnings("unused")
    Base baseField = new Subclass();
  }

  // Placeholder deserializer for Base (not used in serialization tests)
  private static class BaseDeserializer implements JsonDeserializer<Base> {
    @Override
    public Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      throw new AssertionError("Deserialization not supported in this test");
    }
  }

  /**
   * Tests that a registered {@link JsonSerializer} for Base is used during serialization
   * instead of the reflective adapter for the Subclass.
   */
  @Test
  public void serializerForBase_shouldOverrideSubclassReflectiveAdapter() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(
            Base.class,
            (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("custom_serializer")
        )
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).contains("custom_serializer");
    assertThat(json).isEqualTo("{\"baseField\":\"custom_serializer\"}");
  }

  /**
   * Tests that when only a {@link JsonDeserializer} is registered for Base,
   * serialization falls back to reflective handling of the Subclass.
   */
  @Test
  public void deserializerForBase_withoutCustomSerializer_shouldUseReflectiveAdapter() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).contains("test"); // Verify reflective serialization
    assertThat(json).isEqualTo("{\"baseField\":{\"value\":\"test\"}}");
  }

  /**
   * Tests that a custom {@link TypeAdapter} delegate registered for Base
   * takes precedence over reflective serialization of the Subclass.
   */
  @Test
  public void deserializerWithCustomTypeAdapterDelegate_shouldPreferDelegate() {
    Gson gson = new GsonBuilder()
        // Register custom TypeAdapter as the delegate for Base
        .registerTypeAdapter(Base.class, new TypeAdapter<Base>() {
          @Override
          public Base read(JsonReader in) {
            throw new UnsupportedOperationException("Deserialization not supported");
          }

          @Override
          public void write(JsonWriter out, Base value) throws IOException {
            out.value("type_adapter_delegate");
          }
        })
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseField\":\"type_adapter_delegate\"}");
  }

  /**
   * Tests that multiple deserializers falling back to reflective delegation
   * correctly use the reflective adapter for the Subclass during serialization.
   */
  @Test
  public void multipleDeserializersWithReflectiveFallback_shouldUseReflectiveAdapter() {
    Gson gson = new GsonBuilder()
        // Register two deserializers (both delegate to reflective)
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseField\":{\"value\":\"test\"}}");
  }

  /**
   * Tests that a {@link JsonSerializer} delegate takes precedence
   * over reflective serialization of the Subclass.
   */
  @Test
  public void deserializerWithJsonSerializerDelegate_shouldPreferDelegate() {
    Gson gson = new GsonBuilder()
        // Register JsonSerializer as delegate for Base
        .registerTypeAdapter(
            Base.class,
            (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("json_serializer_delegate")
        )
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseField\":\"json_serializer_delegate\"}");
  }

  /**
   * Tests backward compatibility behavior: When a deserializer is registered for Subclass,
   * reflective serialization of Subclass is preferred over a Base serializer.
   * See: https://github.com/google/gson/pull/1787#issuecomment-1222175189
   */
  @Test
  public void deserializerForSubclass_shouldPreventBaseSerializerForBackwardCompatibility() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(
            Subclass.class,
            (JsonDeserializer<Subclass>) (json, typeOfT, context) -> {
              throw new AssertionError("Deserialization not supported in this test");
            })
        .registerTypeAdapter(
            Base.class,
            (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("base_serializer")
        )
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseField\":{\"value\":\"test\"}}");
  }

  // Classes to test cyclic type references during adapter initialization
  private static class CyclicBase {
    @SuppressWarnings("unused")
    CyclicBase nested;
  }

  private static class CyclicSub extends CyclicBase {
    @SuppressWarnings("unused")
    int value;

    CyclicSub(int value) {
      this.value = value;
    }
  }

  /**
   * Tests that Gson correctly handles cyclic types during adapter initialization
   * by using a future adapter mechanism.
   */
  @Test
  public void cyclicTypeDuringAdapterInitialization_shouldSerializeCorrectly() {
    CyclicBase instance = new CyclicBase();
    instance.nested = new CyclicSub(2); // Subclass with field

    String json = new Gson().toJson(instance);
    assertThat(json).isEqualTo("{\"nested\":{\"value\":2}}");
  }
}