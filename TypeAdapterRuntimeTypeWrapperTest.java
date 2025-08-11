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
 * Tests for Gson's TypeAdapterRuntimeTypeWrapper behavior.
 *
 * The core idea: When writing a value declared as Base but holding a Subclass instance,
 * Gson may:
 * - Use a custom serializer registered for Base
 * - Use a custom adapter registered for Base (as a delegate)
 * - Fall back to the reflective adapter for the runtime type (Subclass)
 *
 * These tests document which option is preferred in different registration configurations.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

  // --------- Test model types ---------

  private static class Base {}

  private static class Subclass extends Base {
    String field = "test";
  }

  /** Holds a Base field which actually contains a Subclass instance at runtime. */
  private static class Container {
    Base base = new Subclass();
  }

  /** A deserializer for Base which should never be used in these tests (only for registration). */
  private static class NoOpBaseDeserializer implements JsonDeserializer<Base> {
    @Override
    public Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      throw new AssertionError("Deserializer should not be invoked during these serialization tests");
    }
  }

  // --------- Helpers ---------

  private static JsonSerializer<Base> baseSerializerReturning(String value) {
    return (src, typeOfSrc, context) -> new JsonPrimitive(value);
  }

  private static TypeAdapter<Base> constantStringWritingAdapter(String value) {
    return new TypeAdapter<Base>() {
      @Override
      public Base read(JsonReader in) throws IOException {
        throw new UnsupportedOperationException("read not used in these tests");
      }

      @Override
      public void write(JsonWriter out, Base value) throws IOException {
        out.value(value);
      }
    };
  }

  private static String toJson(Gson gson, Object value) {
    return gson.toJson(value);
  }

  // Expected JSON constants for readability
  private static final String EXPECT_SUBCLASS_REFLECTIVE = "{\"base\":{\"field\":\"test\"}}";
  private static final String EXPECT_SERIALIZER = "{\"base\":\"serializer\"}";
  private static final String EXPECT_CUSTOM_DELEGATE = "{\"base\":\"custom delegate\"}";
  private static final String EXPECT_BASE = "{\"base\":\"base\"}";

  // --------- Tests ---------

  /**
   * If a JsonSerializer is registered for Base, it is preferred over the reflective adapter
   * for the runtime type (Subclass).
   */
  @Test
  public void prefersBaseSerializerOverSubclassReflective() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Base.class, baseSerializerReturning("serializer"))
            .create();

    String json = toJson(gson, new Container());
    assertThat(json).isEqualTo(EXPECT_SERIALIZER);
  }

  /**
   * If only a JsonDeserializer is registered for Base (no serializer), Gson's delegate for Base
   * is the reflective adapter. In this case, writing prefers the reflective adapter for Subclass.
   */
  @Test
  public void fallsBackToSubclassReflectiveWhenOnlyBaseDeserializerRegistered() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Base.class, new NoOpBaseDeserializer())
            .create();

    String json = toJson(gson, new Container());
    assertThat(json).isEqualTo(EXPECT_SUBCLASS_REFLECTIVE);
  }

  /**
   * If Base has a JsonDeserializer and a custom TypeAdapter delegate (which can write),
   * writing prefers the custom delegate over the reflective adapter for Subclass.
   */
  @Test
  public void prefersCustomAdapterDelegateOverSubclassReflective() {
    Gson gson =
        new GsonBuilder()
            // Custom delegate capable of writing
            .registerTypeAdapter(Base.class, constantStringWritingAdapter("custom delegate"))
            // Register a deserializer so that Base uses the above delegate
            .registerTypeAdapter(Base.class, new NoOpBaseDeserializer())
            .create();

    String json = toJson(gson, new Container());
    assertThat(json).isEqualTo(EXPECT_CUSTOM_DELEGATE);
  }

  /**
   * If there are multiple JsonDeserializers registered for Base which all eventually fall back
   * to the reflective adapter as delegate, then writing prefers the reflective adapter for Subclass.
   */
  @Test
  public void multipleDeserializersStillPreferSubclassReflective() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Base.class, new NoOpBaseDeserializer())
            .registerTypeAdapter(Base.class, new NoOpBaseDeserializer())
            .create();

    String json = toJson(gson, new Container());
    assertThat(json).isEqualTo(EXPECT_SUBCLASS_REFLECTIVE);
  }

  /**
   * Backward-compatibility behavior: If a JsonDeserializer is registered for Subclass and a
   * JsonSerializer is registered for Base, Gson still prefers the reflective adapter for Subclass
   * (instead of the Base serializer).
   *
   * See https://github.com/google/gson/pull/1787#issuecomment-1222175189
   */
  @Test
  public void subclassDeserializerTriggersBackwardCompatibility_PreferSubclassReflective() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(
                Subclass.class,
                (JsonDeserializer<Subclass>)
                    (json, typeOfT, context) -> {
                      throw new AssertionError("Subclass deserializer should not be invoked during serialization");
                    })
            .registerTypeAdapter(Base.class, baseSerializerReturning("base"))
            .create();

    String json = toJson(gson, new Container());
    assertThat(json).isEqualTo(EXPECT_SUBCLASS_REFLECTIVE);
  }

  // --------- Cyclic adapter creation case ---------

  private static class CyclicBase {
    CyclicBase next;
  }

  private static class CyclicSub extends CyclicBase {
    int number;

    CyclicSub(int number) {
      this.number = number;
    }
  }

  /**
   * When the adapter for a type is being created (future adapter), Gson defers to the actual
   * adapter later. This ensures runtime typing still works, even with cyclic references.
   */
  @Test
  public void supportsGsonFutureAdapterWithCyclicTypes() {
    CyclicBase root = new CyclicBase();
    root.next = new CyclicSub(2);

    String json = new Gson().toJson(root);
    assertThat(json).isEqualTo("{\"next\":{\"number\":2}}");
  }
}