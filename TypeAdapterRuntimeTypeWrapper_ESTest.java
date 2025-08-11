package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Focused, readable tests for TypeAdapterRuntimeTypeWrapper.
 *
 * These tests avoid EvoSuite scaffolding and exercise the class at a behavioral level:
 * - read() delegates to the wrapped adapter
 * - write() uses the runtime type's adapter when it is more specific
 * - write() keeps the delegate when appropriate
 * - Exceptions from the delegate propagate through the wrapper
 */
public class TypeAdapterRuntimeTypeWrapperTest {

  // --- Simple helper domain types for runtime-type tests ---

  private static class Animal { /* marker */ }
  private static class Dog extends Animal { /* marker */ }

  private static class Base { /* marker */ }
  private static class Sub extends Base { /* marker */ }

  // --- Tests ---

  @Test
  public void read_delegatesToWrappedAdapter() throws Exception {
    // Arrange: a delegate that ignores input and returns a known value
    TypeAdapter<String> delegate = new TypeAdapter<String>() {
      @Override public void write(JsonWriter out, String value) { /* not used */ }
      @Override public String read(JsonReader in) throws IOException {
        // consume the value to keep the reader consistent
        in.nextString();
        return "delegated";
      }
    };
    TypeAdapterRuntimeTypeWrapper<String> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(new Gson(), delegate, String.class);

    JsonReader reader = new JsonReader(new StringReader("\"ignored\""));

    // Act
    String result = wrapper.read(reader);

    // Assert
    assertEquals("delegated", result);
  }

  @Test
  public void write_null_usesDelegateAndWritesJsonNull() throws Exception {
    Gson gson = new Gson();
    TypeAdapter<Object> delegate = gson.getAdapter(Object.class);
    TypeAdapterRuntimeTypeWrapper<Object> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(gson, delegate, Object.class);

    String json = writeToString(wrapper, null);

    assertEquals("null", json);
  }

  @Test
  public void write_prefersRuntimeAdapterWhenMoreSpecific() throws Exception {
    // Arrange: register a custom serializer for Dog
    JsonSerializer<Dog> dogSerializer = (dog, type, ctx) -> new JsonPrimitive("dog");
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Dog.class, dogSerializer)
        .create();

    // Delegate for the declared type (Animal) will be reflective
    TypeAdapter<Animal> animalDelegate = gson.getAdapter(Animal.class);
    TypeAdapterRuntimeTypeWrapper<Animal> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(gson, animalDelegate, Animal.class);

    // Act: write a Dog while the declared type is Animal
    String json = writeToString(wrapper, new Dog());

    // Assert: the Dog serializer should have been used
    assertEquals("\"dog\"", json);
  }

  @Test
  public void write_keepsDelegateWhenRuntimeAdapterIsReflective() throws Exception {
    // Arrange: a non-reflective custom delegate for Base
    TypeAdapter<Base> baseDelegate = new TypeAdapter<Base>() {
      @Override public void write(JsonWriter out, Base value) throws IOException {
        out.value("base");
      }
      @Override public Base read(JsonReader in) { throw new UnsupportedOperationException(); }
    };

    // No special adapter for Sub; Gson will use a reflective adapter for Sub
    Gson gson = new Gson();

    TypeAdapterRuntimeTypeWrapper<Base> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(gson, baseDelegate, Base.class);

    // Act: write a Sub while the declared type is Base
    String json = writeToString(wrapper, new Sub());

    // Assert: because runtime adapter is reflective and delegate is not,
    // the wrapper should keep using the delegate
    assertEquals("\"base\"", json);
  }

  @Test
  public void write_doesNotSwitchWhenDeclaredTypeEqualsRuntimeType() throws Exception {
    // Arrange: a custom delegate for Integer that writes a recognizable value
    TypeAdapter<Integer> intDelegate = new TypeAdapter<Integer>() {
      @Override public void write(JsonWriter out, Integer value) throws IOException {
        out.value("fromDelegate");
      }
      @Override public Integer read(JsonReader in) { throw new UnsupportedOperationException(); }
    };
    Gson gson = new Gson();
    TypeAdapterRuntimeTypeWrapper<Integer> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(gson, intDelegate, Integer.class);

    // Act
    String json = writeToString(wrapper, 42);

    // Assert
    assertEquals("\"fromDelegate\"", json);
  }

  @Test
  public void write_propagatesIOExceptionFromDelegate() throws Exception {
    // Arrange: a delegate that throws on write
    TypeAdapter<String> throwingDelegate = new TypeAdapter<String>() {
      @Override public void write(JsonWriter out, String value) throws IOException {
        throw new IOException("boom");
      }
      @Override public String read(JsonReader in) { throw new UnsupportedOperationException(); }
    };
    Gson gson = new Gson();
    TypeAdapterRuntimeTypeWrapper<String> wrapper =
        new TypeAdapterRuntimeTypeWrapper<>(gson, throwingDelegate, String.class);

    try {
      writeToString(wrapper, "x");
      fail("Expected IOException");
    } catch (IOException expected) {
      assertEquals("boom", expected.getMessage());
    }
  }

  // --- Helpers ---

  private static <T> String writeToString(TypeAdapter<T> adapter, T value) throws IOException {
    StringWriter sink = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(sink);
    adapter.write(jsonWriter, value);
    jsonWriter.flush();
    return sink.toString();
  }
}