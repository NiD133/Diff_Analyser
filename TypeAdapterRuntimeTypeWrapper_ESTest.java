/*
 * Copyright (C) 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may
 * obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class TypeAdapterRuntimeTypeWrapperTest {

  private final Gson gson = new Gson();

  /**
   * The primary purpose of the wrapper is to enrich serialization with runtime
   * type information. For deserialization, it should simply delegate to the
   * wrapped type adapter.
   */
  @Test
  public void testRead_delegatesToWrappedAdapter() throws IOException {
    // Arrange
    TypeAdapter<String> delegateAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) {
        throw new AssertionError("write should not be called");
      }
      @Override
      public String read(JsonReader in) throws IOException {
        return "read-" + in.nextString();
      }
    };
    TypeAdapter<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, String.class);

    // Act
    Object result = wrapper.fromJson("\"test\"");

    // Assert
    assertThat(result).isEqualTo("read-test");
  }

  @Test
  public void testWrite_whenValueIsNull_writesJsonNull() throws IOException {
    // Arrange
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    TypeAdapter<String> delegateAdapter = gson.getAdapter(String.class);
    TypeAdapter<String> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, delegateAdapter, String.class);

    // Act
    wrapper.write(jsonWriter, null);

    // Assert
    assertThat(stringWriter.toString()).isEqualTo("null");
  }

  /**
   * Tests that when the runtime type of the object is the same as the declared
   * type, the wrapper uses the original delegate adapter for serialization.
   */
  @Test
  public void testWrite_whenRuntimeTypeIsSameAsDeclared_usesDelegateAdapter() throws IOException {
    // Arrange
    SpyTypeAdapter<String> spyDelegateAdapter = new SpyTypeAdapter<>();
    TypeAdapter<String> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gson, spyDelegateAdapter, String.class);

    // Act
    wrapper.toJson("hello");

    // Assert
    assertThat(spyDelegateAdapter.wasWriteCalled).isTrue();
  }

  /**
   * This is the main use case for the wrapper. When serializing an object, if its
   * runtime type is more specific than the declared type, the wrapper should
   * look up the adapter for the runtime type and use it.
   */
  @Test
  public void testWrite_whenRuntimeTypeIsMoreSpecific_usesRuntimeTypeAdapter() throws IOException {
    // Arrange
    SpyTypeAdapter<Animal> animalAdapter = new SpyTypeAdapter<>();
    SpyTypeAdapter<Dog> dogAdapter = new SpyTypeAdapter<>();

    Gson gsonWithDogFactory = new GsonBuilder()
        .registerTypeAdapter(Dog.class, dogAdapter)
        .create();

    TypeAdapter<Animal> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gsonWithDogFactory, animalAdapter, Animal.class);
    Animal value = new Dog("Fido");

    // Act
    wrapper.toJson(value);

    // Assert
    assertThat(animalAdapter.wasWriteCalled).isFalse();
    assertThat(dogAdapter.wasWriteCalled).isTrue();
  }

  /**
   * The wrapper has a special check to avoid infinite recursion with reflective
   * adapters. If the delegate is a reflective adapter, it should be used
   * directly, even if a more specific adapter exists for the runtime type.
   */
  @Test
  public void testWrite_whenDelegateIsReflective_usesDelegateAdapter() throws IOException {
    // Arrange
    // This adapter will be used for Dog and will produce a custom JSON format
    TypeAdapter<Dog> customDogAdapter = new TypeAdapter<Dog>() {
      @Override
      public void write(JsonWriter out, Dog value) throws IOException {
        out.value("custom-dog-json");
      }
      @Override
      public Dog read(JsonReader in) {
        throw new AssertionError();
      }
    };

    Gson gsonWithDogAdapter = new GsonBuilder()
        .registerTypeAdapter(Dog.class, customDogAdapter)
        .create();

    // Get a reflective adapter for the base class
    TypeAdapter<Animal> reflectiveAnimalAdapter = gson.getAdapter(Animal.class);
    TypeAdapter<Animal> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gsonWithDogAdapter, reflectiveAnimalAdapter, Animal.class);

    // Act
    String json = wrapper.toJson(new Dog("Fido"));

    // Assert
    // The output should be from the reflective adapter, not the custom one.
    assertThat(json).isEqualTo("{\"name\":\"Fido\"}");
  }

  /**
   * The wrapper also has a special check for TypeVariables. If the declared type
   * is a TypeVariable, it should use the delegate adapter directly without
   * attempting to find a more specific one for the runtime type.
   */
  @Test
  public void testWrite_whenDeclaredTypeIsTypeVariable_usesDelegateAdapter() throws Exception {
    // Arrange
    Type typeVariable = Holder.class.getDeclaredField("value").getGenericType();
    assertThat(typeVariable).isInstanceOf(TypeVariable.class);

    // This adapter will be used for String and will produce a custom JSON format
    TypeAdapter<String> customStringAdapter = new TypeAdapter<String>() {
      @Override
      public void write(JsonWriter out, String value) throws IOException {
        out.value("custom-string-json");
      }
      @Override
      public String read(JsonReader in) {
        throw new AssertionError();
      }
    };

    Gson gsonWithStringAdapter = new GsonBuilder()
        .registerTypeAdapter(String.class, customStringAdapter)
        .create();

    // The delegate will be the default adapter for Object
    TypeAdapter<Object> objectAdapter = gson.getAdapter(Object.class);
    TypeAdapter<Object> wrapper = new TypeAdapterRuntimeTypeWrapper<>(gsonWithStringAdapter, objectAdapter, typeVariable);

    // Act
    String json = wrapper.toJson("hello");

    // Assert
    // The output should be from the Object adapter, not the custom String adapter.
    assertThat(json).isEqualTo("\"hello\"");
  }

  // Helper classes for testing polymorphism
  private static class Animal {
    final String name;
    Animal(String name) {
      this.name = name;
    }
  }

  private static class Dog extends Animal {
    Dog(String name) {
      super(name);
    }
  }

  private static class Holder<T> {
    @SuppressWarnings("unused")
    T value;
  }

  /** A simple spy to verify if the write method was called. */
  private static class SpyTypeAdapter<T> extends TypeAdapter<T> {
    boolean wasWriteCalled = false;

    @Override
    public void write(JsonWriter out, T value) {
      wasWriteCalled = true;
      // Don't write anything to avoid dependency on other adapters
      out.jsonValue("null");
    }

    @Override
    public T read(JsonReader in) {
      throw new AssertionError("read should not be called");
    }
  }
}