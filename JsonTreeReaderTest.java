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

@SuppressWarnings("resource")
public class JsonTreeReaderTest {

  @Test
  public void skipValue_emptyJsonObject_advancesToEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.skipValue();
    
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_filledJsonObject_advancesToEndDocument() throws IOException {
    // Create a complex JSON object with various value types
    JsonObject jsonObject = new JsonObject();
    
    JsonArray jsonArray = new JsonArray();
    jsonArray.add('c');
    jsonArray.add("text");
    jsonObject.add("a", jsonArray);
    
    jsonObject.addProperty("b", true);
    jsonObject.addProperty("i", 1);
    jsonObject.add("n", JsonNull.INSTANCE);
    
    JsonObject innerObject = new JsonObject();
    innerObject.addProperty("n", 2L);
    jsonObject.add("o", innerObject);
    
    jsonObject.addProperty("s", "text");

    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.skipValue();
    
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_onObjectName_skipsToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("a", "value");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    reader.skipValue(); // Skips the object name
    
    // Verify we're positioned at the value
    assertThat(reader.peek()).isEqualTo(JsonToken.STRING);
    assertThat(reader.getPath()).isEqualTo("$.<skipped>");
    assertThat(reader.nextString()).isEqualTo("value");
  }

  @Test
  public void skipValue_afterEndOfDocument_remainsAtEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    
    // Verify initial state
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
    
    reader.skipValue();
    
    // State should remain unchanged
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_atArrayEnd_advancesToEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.skipValue();
    
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_atObjectEnd_advancesToEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.skipValue();
    
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void hasNext_atEndOfDocument_returnsFalse() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    
    assertThat(reader.hasNext()).isFalse();
  }

  @Test
  public void peek_withCustomJsonElementSubclass_throwsException() {
    @SuppressWarnings("deprecation") // superclass constructor
    class CustomSubclass extends JsonElement {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    }

    JsonArray array = new JsonArray();
    array.add(new CustomSubclass());

    JsonTreeReader reader = new JsonTreeReader(array);
    reader.beginArray();

    // Verify custom subclass causes expected exception
    var e = assertThrows(MalformedJsonException.class, reader::peek);
    assertThat(e)
        .hasMessageThat()
        .isEqualTo(
            "Custom JsonElement subclass " + CustomSubclass.class.getName() + " is not supported");
  }

  /**
   * {@link JsonTreeReader} ignores nesting limit because:
   *
   * <ul>
   *   <li>It is an internal class and often created implicitly without the user having access to it
   *       (as {@link JsonReader}), so they cannot easily adjust the limit
   *   <li>{@link JsonTreeReader} may be created based on an existing {@link JsonReader}; in that
   *       case it would be necessary to propagate settings to account for a custom nesting limit,
   *       see also related https://github.com/google/gson/pull/2151
   *   <li>Nesting limit as protection against {@link StackOverflowError} is not that relevant for
   *       {@link JsonTreeReader} because a deeply nested {@link JsonElement} tree would first have
   *       to be constructed; and if it is constructed from a regular {@link JsonReader}, then its
   *       nesting limit would already apply
   * </ul>
   */
  @Test
  public void nestingLimit_whenSet_ignoredForDeeplyNestedArrays() throws IOException {
    int limit = 10;
    // Create nested arrays (limit + 1 levels deep)
    JsonArray root = new JsonArray();
    JsonArray current = root;
    for (int i = 0; i < limit; i++) {
      JsonArray child = new JsonArray();
      current.add(child);
      current = child;
    }

    JsonTreeReader reader = new JsonTreeReader(root);
    reader.setNestingLimit(limit);
    assertThat(reader.getNestingLimit()).isEqualTo(limit);

    // Traverse all nested arrays (limit+1 beginArray calls)
    for (int i = 0; i < limit + 1; i++) {
      reader.beginArray();
    }

    // Close all arrays
    for (int i = 0; i < limit + 1; i++) {
      reader.endArray();
    }

    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    reader.close();
  }

  /**
   * {@link JsonTreeReader} effectively replaces the complete reading logic of {@link JsonReader} to
   * read from a {@link JsonElement} instead of a {@link Reader}. Therefore all relevant methods of
   * {@code JsonReader} must be overridden.
   */
  @Test
  public void overrides_jsonReaderMethods_exceptIgnoredOnes() {
    List<String> ignoredMethods =
        Arrays.asList(
            "setLenient(boolean)",
            "isLenient()",
            "setStrictness(com.google.gson.Strictness)",
            "getStrictness()",
            "setNestingLimit(int)",
            "getNestingLimit()");
    MoreAsserts.assertOverridesMethods(JsonReader.class, JsonTreeReader.class, ignoredMethods);
  }
}