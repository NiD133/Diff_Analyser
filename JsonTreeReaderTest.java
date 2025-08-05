/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for JsonTreeReader, which reads JSON data from JsonElement objects
 * rather than from character streams.
 */
@SuppressWarnings("resource")
public class JsonTreeReaderTest {

  // Constants for better readability
  private static final String ROOT_PATH = "$";
  private static final String SKIPPED_PATH = "$.<skipped>";

  @Test
  public void testSkipValue_emptyJsonObject_shouldSkipToEndOfDocument() throws IOException {
    // Given: An empty JSON object
    JsonObject emptyObject = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(emptyObject);

    // When: We skip the entire value
    reader.skipValue();

    // Then: Reader should be at end of document with root path
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo(ROOT_PATH);
  }

  @Test
  public void testSkipValue_complexJsonObject_shouldSkipToEndOfDocument() throws IOException {
    // Given: A complex JSON object with various data types
    JsonObject complexObject = createComplexJsonObject();
    JsonTreeReader reader = new JsonTreeReader(complexObject);

    // When: We skip the entire value
    reader.skipValue();

    // Then: Reader should be at end of document with root path
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo(ROOT_PATH);
  }

  @Test
  public void testSkipValue_propertyName_shouldSkipNameAndPointToValue() throws IOException {
    // Given: A JSON object with a property
    JsonObject objectWithProperty = new JsonObject();
    objectWithProperty.addProperty("propertyName", "propertyValue");
    JsonTreeReader reader = new JsonTreeReader(objectWithProperty);

    // When: We begin reading the object and skip the property name
    reader.beginObject();
    reader.skipValue(); // This skips the property name

    // Then: Reader should point to the property value
    assertThat(reader.peek()).isEqualTo(JsonToken.STRING);
    assertThat(reader.getPath()).isEqualTo(SKIPPED_PATH);
    assertThat(reader.nextString()).isEqualTo("propertyValue");
  }

  @Test
  public void testSkipValue_afterEndOfDocument_shouldRemainAtEndOfDocument() throws IOException {
    // Given: A reader that has already reached end of document
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);

    // When: We try to skip a value after end of document
    String pathBeforeSkip = reader.getPath();
    reader.skipValue();

    // Then: Reader should remain at end of document with unchanged path
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo(pathBeforeSkip);
    assertThat(reader.getPath()).isEqualTo(ROOT_PATH);
  }

  @Test
  public void testSkipValue_emptyArray_shouldSkipToEndOfDocument() throws IOException {
    // Given: An empty JSON array
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());

    // When: We begin reading the array and skip its content
    reader.beginArray();
    reader.skipValue();

    // Then: Reader should be at end of document
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo(ROOT_PATH);
  }

  @Test
  public void testSkipValue_emptyObject_shouldSkipToEndOfDocument() throws IOException {
    // Given: An empty JSON object
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());

    // When: We begin reading the object and skip its content
    reader.beginObject();
    reader.skipValue();

    // Then: Reader should be at end of document
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo(ROOT_PATH);
  }

  @Test
  public void testHasNext_atEndOfDocument_shouldReturnFalse() throws IOException {
    // Given: A reader at end of document
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();

    // When/Then: hasNext() should return false
    assertThat(reader.hasNext()).isFalse();
  }

  @Test
  public void testCustomJsonElementSubclass_shouldThrowMalformedJsonException() throws IOException {
    // Given: A custom JsonElement subclass (which is not supported)
    @SuppressWarnings("deprecation") // superclass constructor
    class CustomJsonElementSubclass extends JsonElement {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    }

    JsonArray arrayWithCustomElement = new JsonArray();
    arrayWithCustomElement.add(new CustomJsonElementSubclass());
    JsonTreeReader reader = new JsonTreeReader(arrayWithCustomElement);

    // When: We try to read the custom element
    reader.beginArray();

    // Then: Should throw MalformedJsonException with descriptive message
    MalformedJsonException exception = assertThrows(
        MalformedJsonException.class, 
        () -> reader.peek()
    );
    
    String expectedMessage = "Custom JsonElement subclass " 
        + CustomJsonElementSubclass.class.getName() + " is not supported";
    assertThat(exception).hasMessageThat().isEqualTo(expectedMessage);
  }

  /**
   * JsonTreeReader ignores nesting limits for the following reasons:
   * 
   * 1. It's an internal class often created implicitly, so users can't easily adjust limits
   * 2. When created from an existing JsonReader, propagating settings would be complex
   * 3. Nesting limit protection is less relevant since deeply nested JsonElement trees
   *    must be constructed first, and regular JsonReaders would already apply limits
   */
  @Test
  public void testNestingLimit_shouldBeIgnored() throws IOException {
    // Given: A deeply nested JSON structure that exceeds the nesting limit
    int nestingLimit = 10;
    JsonArray deeplyNestedArray = createDeeplyNestedArray(nestingLimit + 1);
    
    JsonTreeReader reader = new JsonTreeReader(deeplyNestedArray);
    reader.setNestingLimit(nestingLimit);
    assertThat(reader.getNestingLimit()).isEqualTo(nestingLimit);

    // When: We read beyond the nesting limit
    for (int depth = 0; depth <= nestingLimit; depth++) {
      reader.beginArray(); // This should not throw despite exceeding limit
    }

    // Then: Reader should successfully navigate the structure
    reader.endArray();
    for (int depth = 0; depth < nestingLimit; depth++) {
      reader.endArray();
    }
    
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    reader.close();
  }

  /**
   * JsonTreeReader must override all relevant JsonReader methods because it completely
   * replaces the reading logic to work with JsonElement instead of Reader.
   */
  @Test
  public void testMethodOverrides_shouldOverrideAllRelevantJsonReaderMethods() {
    // Given: Methods that are intentionally not overridden
    List<String> methodsNotRequiringOverride = Arrays.asList(
        "setLenient(boolean)",
        "isLenient()",
        "setStrictness(com.google.gson.Strictness)",
        "getStrictness()",
        "setNestingLimit(int)",
        "getNestingLimit()"
    );

    // When/Then: All other methods should be overridden
    MoreAsserts.assertOverridesMethods(
        JsonReader.class, 
        JsonTreeReader.class, 
        methodsNotRequiringOverride
    );
  }

  // Helper methods for better test data creation

  /**
   * Creates a complex JSON object with various data types for testing.
   * Structure: {"a": ['c', "text"], "b": true, "i": 1, "n": null, "o": {"n": 2}, "s": "text"}
   */
  private JsonObject createComplexJsonObject() {
    JsonObject complexObject = new JsonObject();
    
    // Add array property
    JsonArray jsonArray = new JsonArray();
    jsonArray.add('c');
    jsonArray.add("text");
    complexObject.add("a", jsonArray);
    
    // Add various primitive properties
    complexObject.addProperty("b", true);
    complexObject.addProperty("i", 1);
    complexObject.add("n", JsonNull.INSTANCE);
    
    // Add nested object property
    JsonObject nestedObject = new JsonObject();
    nestedObject.addProperty("n", 2L);
    complexObject.add("o", nestedObject);
    
    complexObject.addProperty("s", "text");
    
    return complexObject;
  }

  /**
   * Creates a deeply nested JSON array structure for nesting limit tests.
   * 
   * @param depth The depth of nesting to create
   * @return A JsonArray nested to the specified depth
   */
  private JsonArray createDeeplyNestedArray(int depth) {
    JsonArray rootArray = new JsonArray();
    JsonArray currentArray = rootArray;
    
    for (int i = 0; i < depth; i++) {
      JsonArray nestedArray = new JsonArray();
      currentArray.add(nestedArray);
      currentArray = nestedArray;
    }
    
    return rootArray;
  }
}