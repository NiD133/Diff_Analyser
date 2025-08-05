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
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

@SuppressWarnings("resource") // Closing JsonTreeReader is not necessary
public final class JsonTreeReaderTest {

  @Test
  public void skipValue_onEmptyObject_reachesEndDocument() throws IOException {
    // Arrange
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());

    // Act
    reader.skipValue();

    // Assert
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_onEmptyArray_reachesEndDocument() throws IOException {
    // Arrange
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());

    // Act
    reader.skipValue();

    // Assert
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_onPopulatedObject_reachesEndDocument() throws IOException {
    // Arrange
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add('c');
    jsonArray.add("text");
    jsonObject.add("a", jsonArray);
    jsonObject.addProperty("b", true);
    jsonObject.addProperty("i", 1);
    jsonObject.add("n", JsonNull.INSTANCE);
    JsonObject jsonObject2 = new JsonObject();
    jsonObject2.addProperty("n", 2L);
    jsonObject.add("o", jsonObject2);
    jsonObject.addProperty("s", "text");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    // Act
    reader.skipValue();

    // Assert
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_whenAtName_advancesToValue() throws IOException {
    // Arrange
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("a", "value");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();

    // Act: When the next token is NAME, skipValue() consumes only the name.
    // This is a specific behavior of JsonTreeReader; the general JsonReader contract
    // implies that the corresponding value would be skipped as well.
    reader.skipValue();

    // Assert
    assertThat(reader.peek()).isEqualTo(JsonToken.STRING);
    assertThat(reader.getPath()).isEqualTo("$.<skipped>");
    assertThat(reader.nextString()).isEqualTo("value");
  }

  @Test
  public void skipValue_afterEndDocument_isNoOp() throws IOException {
    // Arrange
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");

    // Act
    reader.skipValue();

    // Assert: State remains unchanged
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void hasNext_atEndOfDocument_returnsFalse() throws IOException {
    // Arrange
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();

    // Act & Assert
    assertThat(reader.hasNext()).isFalse();
  }

  @Test
  public void peek_onCustomJsonElementSubclass_throwsException() throws IOException {
    // Arrange
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

    // Act & Assert
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
  public void nestingLimit_isIgnored() throws IOException {
    // Arrange
    int limit = 10;
    JsonArray json = new JsonArray();
    JsonArray current = json;
    // Create a deeply nested structure exceeding the limit
    for (int i = 0; i < limit; i++) {
      JsonArray nested = new JsonArray();
      current.add(nested);
      current = nested;
    }

    JsonTreeReader reader = new JsonTreeReader(json);
    reader.setNestingLimit(limit);
    assertThat(reader.getNestingLimit()).isEqualTo(limit);

    // Act: Traverse the structure
    for (int i = 0; i < limit; i++) {
      reader.beginArray();
    }
    // Assert: Exceeding the limit does not throw an exception
    reader.beginArray();

    // Cleanup
    reader.endArray();
    for (int i = 0; i < limit; i++) {
      reader.endArray();
    }
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    reader.close();
  }

  /**
   * {@link JsonTreeReader} effectively replaces the complete reading logic of {@link JsonReader} to
   * read from a {@link JsonElement} instead of a standard {@link java.io.Reader}. Therefore, all
   * relevant methods of {@code JsonReader} must be overridden.
   */
  @Test
  public void jsonTreeReader_overridesRequiredMethods() {
    // Arrange
    List<String> ignoredMethods =
        Arrays.asList(
            "setLenient(boolean)",
            "isLenient()",
            "setStrictness(com.google.gson.Strictness)",
            "getStrictness()",
            "setNestingLimit(int)",
            "getNestingLimit()");

    // Act & Assert
    MoreAsserts.assertOverridesMethods(JsonReader.class, JsonTreeReader.class, ignoredMethods);
  }
}