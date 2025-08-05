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
 * Tests for {@code TypeAdapterRuntimeTypeWrapper}, which handles polymorphic serialization.
 *
 * <p>These tests verify which adapter Gson selects when the runtime type of a value (e.g.
 * {@code Subclass}) is more specific than its declared type (e.g. {@code Base}). The choice
 * depends on whether a custom adapter is registered for the base type and whether that adapter is
 * considered 'reflective'.
 */
public class TypeAdapterRuntimeTypeWrapperTest {
  // Expected JSON output when the Subclass is serialized using its reflective adapter
  private static final String SUBCLASS_REFLECTIVE_JSON = "{\"b\":{\"f\":\"test\"}}";
  // Expected JSON output when a custom serializer for the Base class is used
  private static final String BASE_CUSTOM_SERIALIZER_JSON = "{\"b\":\"serializer\"}";
  // Expected JSON output when a custom delegate adapter for the Base class is used
  private static final String BASE_CUSTOM_DELEGATE_JSON = "{\"b\":\"custom delegate\"}";

  // Test fixtures

  private static class Base {}

  private static class Subclass extends Base {
    @SuppressWarnings("unused")
    String f = "test";
  }

  /** Contains a field of the base type, but holds an instance of the subclass. */
  private static class Container {
    @SuppressWarnings("unused")
    Base b = new Subclass();
  }

  /** A deserializer which is not intended to be called in these serialization tests. */
  private static class DeserializerOnly implements JsonDeserializer<Base> {
    @Override
    public Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      throw new AssertionError("Deserializer should not be called during serialization test");
    }
  }

  /**
   * Verifies that a custom {@link JsonSerializer} for a base type is used for serializing a
   * subclass instance.
   */
  @Test
  public void serialization_usesBaseTypeSerializer_forSubclassInstance() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(
                Base.class,
                (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("serializer"))
            .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo(BASE_CUSTOM_SERIALIZER_JSON);
  }

  /**
   * Verifies that when only a {@link JsonDeserializer} is registered for the base type, Gson falls
   * back to the reflective serializer for the base type. This 'reflective' nature causes Gson to
   * prefer the more specific reflective adapter for the subclass at runtime.
   */
  @Test
  public void serialization_usesSubclassReflectiveAdapter_whenBaseTypeHasDeserializerOnly() {
    Gson gson = new GsonBuilder().registerTypeAdapter(Base.class, new DeserializerOnly()).create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo(SUBCLASS_REFLECTIVE_JSON);
  }

  /**
   * Verifies that when a full {@link TypeAdapter} with a custom serializer is registered for the
   * base type, its serializer is preferred over the subclass's reflective adapter, even if a
   * separate {@link JsonDeserializer} is also registered.
   */
  @Test
  public void serialization_usesBaseTypeCustomDelegateAdapter_forSubclassInstance() {
    Gson gson =
        new GsonBuilder()
            // Register a full TypeAdapter with a custom serializer (write method)
            .registerTypeAdapter(
                Base.class,
                new TypeAdapter<Base>() {
                  @Override
                  public void write(JsonWriter out, Base value) throws IOException {
                    out.value("custom delegate");
                  }

                  @Override
                  public Base read(JsonReader in) {
                    throw new UnsupportedOperationException();
                  }
                })
            // This registration for the same type only affects deserialization
            .registerTypeAdapter(Base.class, new DeserializerOnly())
            .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo(BASE_CUSTOM_DELEGATE_JSON);
  }

  /**
   * Verifies that when multiple {@link JsonDeserializer}s are registered for the base type, Gson
   * still falls back to reflective serialization, causing it to prefer the subclass's reflective
   * adapter.
   */
  @Test
  public void serialization_usesSubclassReflectiveAdapter_whenBaseTypeHasMultipleDeserializers() {
    Gson gson =
        new GsonBuilder()
            // Registering multiple deserializers still results in a reflective serializer
            .registerTypeAdapter(Base.class, new DeserializerOnly())
            .registerTypeAdapter(Base.class, new DeserializerOnly())
            .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo(SUBCLASS_REFLECTIVE_JSON);
  }

  /**
   * Verifies that if a {@link JsonSerializer} is provided as a delegate for the base type, it is
   * preferred over the subclass's reflective adapter.
   */
  @Test
  public void serialization_usesBaseTypeSerializerDelegate_forSubclassInstance() {
    Gson gson =
        new GsonBuilder()
            // Register a custom JsonSerializer as a delegate
            .registerTypeAdapter(
                Base.class,
                (JsonSerializer<Base>)
                    (src, typeOfSrc, context) -> new JsonPrimitive("custom delegate"))
            // Registering a deserializer alongside it does not change serialization behavior
            .registerTypeAdapter(Base.class, new DeserializerOnly())
            .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo(BASE_CUSTOM_DELEGATE_JSON);
  }

  /**
   * Verifies a backward compatibility case: if any adapter is registered for the subclass, Gson
   * prefers the subclass's reflective adapter for serialization, even if a custom serializer
   * exists for the base class.
   *
   * @see <a href="https://github.com/google/gson/pull/1787#issuecomment-1222175189">GitHub
   *     Comment</a>
   */
  @Test
  public void serialization_prefersSubclassReflectiveAdapter_forBackwardCompatibility() {
    Gson gson =
        new GsonBuilder()
            // Registering any adapter for the subclass triggers this behavior
            .registerTypeAdapter(
                Subclass.class,
                (JsonDeserializer<Subclass>)
                    (json, typeOfT, context) -> {
                      throw new AssertionError("not needed for this test");
                    })
            // This custom serializer for the base class would normally be used, but is ignored
            .registerTypeAdapter(
                Base.class,
                (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("base"))
            .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo(SUBCLASS_REFLECTIVE_JSON);
  }

  // Test fixtures for cyclic type test

  private static class CyclicBase {
    @SuppressWarnings("unused")
    CyclicBase f;
  }

  private static class CyclicSub extends CyclicBase {
    @SuppressWarnings("unused")
    int i;

    public CyclicSub(int i) {
      this.i = i;
    }
  }

  /**
   * Tests that runtime type resolution works correctly even for cyclic types where Gson uses a
   * future-style adapter internally to break the creation cycle.
   */
  @Test
  public void serialization_handlesRuntimeType_withCyclicTypes() {
    // Arrange
    CyclicBase cyclicObject = new CyclicBase();
    cyclicObject.f = new CyclicSub(2);
    Gson gson = new Gson();

    // Act
    String json = gson.toJson(cyclicObject);

    // Assert
    assertThat(json).isEqualTo("{\"f\":{\"i\":2}}");
  }
}