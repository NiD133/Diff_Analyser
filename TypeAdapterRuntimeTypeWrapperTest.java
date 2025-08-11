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

  // Base class for testing
  private static class Base {}

  // Subclass extending Base
  private static class Subclass extends Base {
    @SuppressWarnings("unused")
    String field = "test";
  }

  // Container class holding a Base instance
  private static class Container {
    @SuppressWarnings("unused")
    Base baseInstance = new Subclass();
  }

  // Custom deserializer for Base class
  private static class BaseDeserializer implements JsonDeserializer<Base> {
    @Override
    public Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      throw new AssertionError("Deserialization not needed for this test");
    }
  }

  /**
   * Test to verify that a custom JsonSerializer for Base is preferred over the reflective adapter
   * for Subclass during serialization.
   */
  @Test
  public void testCustomJsonSerializer() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(
            Base.class,
            (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("serializer"))
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseInstance\":\"serializer\"}");
  }

  /**
   * Test to verify that when only a JsonDeserializer is registered for Base, the reflective adapter
   * for Subclass is used during serialization.
   */
  @Test
  public void testReflectiveSerializerWithDeserializer() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseInstance\":{\"field\":\"test\"}}");
  }

  /**
   * Test to verify that a custom TypeAdapter delegate for Base is preferred over the reflective
   * adapter for Subclass during serialization.
   */
  @Test
  public void testCustomSerializerWithDeserializerDelegate() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(
            Base.class,
            new TypeAdapter<Base>() {
              @Override
              public Base read(JsonReader in) throws IOException {
                throw new UnsupportedOperationException();
              }

              @Override
              public void write(JsonWriter out, Base value) throws IOException {
                out.value("custom delegate");
              }
            })
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseInstance\":\"custom delegate\"}");
  }

  /**
   * Test to verify that when multiple JsonDeserializers are registered for Base, the reflective
   * adapter for Subclass is used during serialization.
   */
  @Test
  public void testMultipleDeserializersWithReflectiveSerialization() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseInstance\":{\"field\":\"test\"}}");
  }

  /**
   * Test to verify that a JsonSerializer delegate for Base is preferred over the reflective adapter
   * for Subclass during serialization.
   */
  @Test
  public void testJsonSerializerDelegateWithDeserializer() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(
            Base.class,
            (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("custom delegate"))
        .registerTypeAdapter(Base.class, new BaseDeserializer())
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseInstance\":\"custom delegate\"}");
  }

  /**
   * Test to verify backward compatibility when a JsonDeserializer is registered for Subclass and a
   * custom JsonSerializer is registered for Base.
   */
  @Test
  public void testBackwardCompatibilityWithSubclassDeserializer() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(
            Subclass.class,
            (JsonDeserializer<Subclass>) (json, typeOfT, context) -> {
              throw new AssertionError("Deserialization not needed for this test");
            })
        .registerTypeAdapter(
            Base.class,
            (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("base"))
        .create();

    String json = gson.toJson(new Container());
    assertThat(json).isEqualTo("{\"baseInstance\":{\"field\":\"test\"}}");
  }

  // Cyclic base class for testing future adapter behavior
  private static class CyclicBase {
    @SuppressWarnings("unused")
    CyclicBase cyclicField;
  }

  // Cyclic subclass extending CyclicBase
  private static class CyclicSub extends CyclicBase {
    @SuppressWarnings("unused")
    int integerField;

    public CyclicSub(int integerField) {
      this.integerField = integerField;
    }
  }

  /**
   * Test to verify behavior when the type of a field refers to a type whose adapter is being
   * created. Gson uses a future adapter for such cases.
   */
  @Test
  public void testFutureAdapterBehavior() {
    CyclicBase baseInstance = new CyclicBase();
    baseInstance.cyclicField = new CyclicSub(2);

    String json = new Gson().toJson(baseInstance);
    assertThat(json).isEqualTo("{\"cyclicField\":{\"integerField\":2}}");
  }
}